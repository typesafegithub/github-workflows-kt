package io.github.typesafegithub.workflows.jitbindingserver

import com.sksamuel.aedile.core.LoadingCache
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.github.typesafegithub.workflows.mavenbinding.JarArtifact
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.httpMethod
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.route
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

private val logger = logger { }

typealias CachedVersionArtifact = Optional<VersionArtifacts>

private val prefetchScope = CoroutineScope(Dispatchers.IO)

fun Routing.artifactRoutes(
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
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
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
    refresh: Boolean = false,
) {
    headArtifact(bindingsCache, prometheusRegistry, refresh)
    getArtifact(bindingsCache, prometheusRegistry, refresh)
}

private fun Route.headArtifact(
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
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
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
    prometheusRegistry: PrometheusMeterRegistry?,
    refresh: Boolean,
) {
    get {
        // To fix: there'a problem with caching the artifacts under certain keys,
        //   and parameters.extractActionCoords returning certain (different) ActionCoords.
        //   Because of this problem, the artifact that uses commit pinning is now not found.
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
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
) {
    prefetchScope.launch {
        bindingsCache.getAll(
            coords.map {
                BindingsServerRequest(
                    rawName = it.name,
                    rawVersion = it.version,
                    actionCoords = it,
                )
            },
        )
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(
    refresh: Boolean,
    bindingsCache: LoadingCache<BindingsServerRequest, CachedVersionArtifact>,
): VersionArtifacts? {
    val parsedRequest = parameters.parseRequest(extractVersion = true)

    logger.info { "➡️ Requesting ${parsedRequest.actionCoords.prettyPrint}" }
    if (refresh) {
        bindingsCache.invalidate(parsedRequest)
    }
    return bindingsCache.get(parsedRequest).getOrNull()
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
