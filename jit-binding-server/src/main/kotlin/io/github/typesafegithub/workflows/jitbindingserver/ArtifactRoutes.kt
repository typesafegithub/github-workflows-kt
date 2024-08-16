package io.github.typesafegithub.workflows.jitbindingserver

import com.sksamuel.aedile.core.LoadingCache
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.mavenbinding.JarArtifact
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID

private val logger = logger { }

typealias CachedVersionArtifact = VersionArtifacts?

private val prefetchScope = CoroutineScope(Dispatchers.IO)

fun Routing.artifactRoutes(
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    prometheusRegistry: PrometheusMeterRegistry? = null,
) {
    prometheusRegistry?.let {
        CaffeineCacheMetrics.monitor(it, bindingsCache.underlying(), "bindings_cache")
    }

    route("{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, bindingsCache, refresh = false)
    }

    route("/refresh/{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, bindingsCache, refresh = true)
    }
}

private fun Route.artifact(
    prometheusRegistry: PrometheusMeterRegistry?,
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    refresh: Boolean = false,
) {
    headArtifact(bindingsCache, prometheusRegistry, refresh)
    getArtifact(bindingsCache, prometheusRegistry, refresh)
    postArtifact(bindingsCache, prometheusRegistry)
}

private fun Route.headArtifact(
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    prometheusRegistry: PrometheusMeterRegistry?,
    refresh: Boolean,
) {
    head {
        val bindingArtifacts = call.toBindingArtifacts(refresh, bindingsCache) ?: return@head call.respondNotFound()

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in bindingArtifacts.files) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }

        prometheusRegistry?.incrementArtifactCounter(call, bindingArtifacts.typingActualSource)
    }
}

private fun Route.getArtifact(
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    prometheusRegistry: PrometheusMeterRegistry?,
    refresh: Boolean,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh, bindingsCache) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        val artifact = bindingArtifacts.files[file] ?: return@get call.respondNotFound()

        when (artifact) {
            is TextArtifact -> call.respondText(text = artifact.data())
            is JarArtifact -> call.respondBytes(artifact.data(), ContentType.parse("application/java-archive"))
        }

        prometheusRegistry?.incrementArtifactCounter(call, bindingArtifacts.typingActualSource)
    }
}

internal fun prefetchBindingArtifacts(
    coords: Collection<ActionCoords>,
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
) {
    prefetchScope.launch {
        bindingsCache.getAll(coords)
    }
}

private fun Route.postArtifact(
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    prometheusRegistry: PrometheusMeterRegistry?,
) {
    post {
        val owner = "${call.parameters["owner"]}__types__${randomUUID()}"
        val name = call.parameters["name"]!!
        val version = call.parameters["version"]!!
        val types = call.receiveText()
        runCatching {
            Load().loadOne(types)
        }.onFailure {
            call.respondText(
                text = "Exception while parsing supplied typings:\n${it.stackTraceToString()}",
                status = HttpStatusCode.UnprocessableEntity,
            )
            return@post
        }
        val typingActualSource =
            call
                .toBindingArtifacts(
                    refresh = true,
                    bindingsCache = bindingsCache,
                    owner = owner,
                    types = types,
                )?.typingActualSource ?: TypingActualSource.CUSTOM
        call.respondText(text = "$owner:$name:$version")

        prometheusRegistry?.incrementArtifactCounter(call, typingActualSource)
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(
    refresh: Boolean,
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    owner: String = parameters["owner"]!!,
    types: String? = null,
): VersionArtifacts? {
    val actionCoords = parameters.extractActionCoords(extractVersion = true, owner = owner)

    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    if (refresh) {
        bindingsCache.invalidate(actionCoords)
    }
    return (
        if (types != null) {
            bindingsCache.get(actionCoords) { buildVersionArtifacts(actionCoords, types) }
        } else {
            bindingsCache.get(actionCoords)
        }
    )
}

private fun PrometheusMeterRegistry.incrementArtifactCounter(
    call: ApplicationCall,
    typingActualSource: TypingActualSource?,
) {
    val owner = call.parameters["owner"] ?: "unknown"
    val name = call.parameters["name"] ?: "unknown"
    val version = call.parameters["version"] ?: "unknown"
    val file = call.parameters["file"] ?: "unknown"
    val method = call.request.httpMethod.value
    val status =
        call.response
            .status()
            ?.value
            ?.toString() ?: "unknown"
    val typingActualSourceString =
        when (typingActualSource) {
            TypingActualSource.ACTION -> "action"
            TypingActualSource.TYPING_CATALOG -> "typing_catalog"
            TypingActualSource.CUSTOM -> "custom"
            null -> "no_typing"
        }

    val counter =
        this.counter(
            "artifact_requests_total",
            listOf(
                Tag.of("owner", owner),
                Tag.of("name", name),
                Tag.of("version", version),
                Tag.of("file", file),
                Tag.of("method", method),
                Tag.of("status", status),
                Tag.of("typing_actual_source", typingActualSourceString),
            ),
        )
    counter.increment()
}
