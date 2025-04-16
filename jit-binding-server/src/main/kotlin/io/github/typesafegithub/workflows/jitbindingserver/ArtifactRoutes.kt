package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours

private val logger = logger { }

typealias ArtifactResult = Result<Map<String, Artifact>>

private val bindingsCache =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, ArtifactResult> { runCatching { it.buildVersionArtifacts()!! } }

fun Routing.artifactRoutes(prometheusRegistry: PrometheusMeterRegistry) {
    CaffeineCacheMetrics.monitor(prometheusRegistry, bindingsCache.underlying(), "bindings_cache")

    route("{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, refresh = false)
    }

    route("/refresh/{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, refresh = true)
    }
}

private fun Route.artifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean = false,
) {
    headArtifact(prometheusRegistry, refresh)
    getArtifact(prometheusRegistry, refresh)
}

private fun Route.headArtifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean,
) {
    head {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@head call.respondNotFound()

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in bindingArtifacts) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }

        incrementArtifactCounter(prometheusRegistry, call)
    }
}

private fun Route.getArtifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        val artifact = bindingArtifacts[file] ?: return@get call.respondNotFound()

        when (artifact) {
            is TextArtifact -> call.respondText(text = artifact.data())
            is JarArtifact -> call.respondBytes(artifact.data(), ContentType.parse("application/java-archive"))
        }

        incrementArtifactCounter(prometheusRegistry, call)
    }
}

@OptIn(DelicateCoroutinesApi::class)
internal fun prefetchBindingArtifacts(coords: Collection<ActionCoords>) {
    GlobalScope.launch {
        bindingsCache.getAll(coords)
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(refresh: Boolean): Map<String, Artifact>? {
    val actionCoords = parameters.extractActionCoords(extractVersion = true)

    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    if (refresh) {
        bindingsCache.invalidate(actionCoords)
    }
    return bindingsCache.get(actionCoords).getOrNull()
}

private fun incrementArtifactCounter(
    prometheusRegistry: PrometheusMeterRegistry,
    call: ApplicationCall,
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

    val counter =
        prometheusRegistry.counter(
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
