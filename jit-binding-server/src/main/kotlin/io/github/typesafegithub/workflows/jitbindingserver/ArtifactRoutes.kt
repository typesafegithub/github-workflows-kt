package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.LoadingCache
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.refreshAfterWrite
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.mavenbinding.Artifact
import io.github.typesafegithub.workflows.mavenbinding.JarArtifact
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
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
import kotlin.time.Duration.Companion.hours

private val logger = logger { }

typealias ArtifactResult = Result<Map<String, Artifact>>

private val prefetchScope = CoroutineScope(Dispatchers.IO)

internal fun buildBindingsCache(
    buildVersionArtifacts: (ActionCoords) -> Map<String, Artifact>? = ::buildVersionArtifacts,
): LoadingCache<ActionCoords, ArtifactResult> =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, ArtifactResult> { runCatching { buildVersionArtifacts(it)!! } }

fun Routing.artifactRoutes(
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
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
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
    refresh: Boolean = false,
) {
    headArtifact(bindingsCache, prometheusRegistry, refresh)
    getArtifact(bindingsCache, prometheusRegistry, refresh)
}

private fun Route.headArtifact(
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
    prometheusRegistry: PrometheusMeterRegistry?,
    refresh: Boolean,
) {
    head {
        val bindingArtifacts = call.toBindingArtifacts(refresh, bindingsCache) ?: return@head call.respondNotFound()

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in bindingArtifacts) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }

        prometheusRegistry?.incrementArtifactCounter(call)
    }
}

private fun Route.getArtifact(
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
    prometheusRegistry: PrometheusMeterRegistry?,
    refresh: Boolean,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh, bindingsCache) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        val artifact = bindingArtifacts[file] ?: return@get call.respondNotFound()

        when (artifact) {
            is TextArtifact -> call.respondText(text = artifact.data())
            is JarArtifact -> call.respondBytes(artifact.data(), ContentType.parse("application/java-archive"))
        }

        prometheusRegistry?.incrementArtifactCounter(call)
    }
}

internal fun prefetchBindingArtifacts(
    coords: Collection<ActionCoords>,
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
) {
    prefetchScope.launch {
        bindingsCache.getAll(coords)
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(
    refresh: Boolean,
    bindingsCache: LoadingCache<ActionCoords, ArtifactResult>,
): Map<String, Artifact>? {
    val actionCoords = parameters.extractActionCoords(extractVersion = true)

    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    if (refresh) {
        bindingsCache.invalidate(actionCoords)
    }
    return bindingsCache.get(actionCoords).getOrNull()
}

private fun PrometheusMeterRegistry.incrementArtifactCounter(call: ApplicationCall) {
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
            ),
        )
    counter.increment()
}
