package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.reactivecircus.cache4k.Cache
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
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.time.Duration.Companion.hours

private val logger = logger { }

typealias ArtifactResult = Result<Map<String, Artifact>>

private val bindingsCache = Cache.Builder<ActionCoords, ArtifactResult>().expireAfterWrite(1.hours).build()

fun Routing.artifactRoutes(prometheusRegistry: PrometheusMeterRegistry) {
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

        incrementArtifactCounter(prometheusRegistry, call)

        if (file in bindingArtifacts) {
            call.respondText("Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }
    }
}

private fun Route.getArtifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText("OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        incrementArtifactCounter(prometheusRegistry, call)

        val artifact = bindingArtifacts[file] ?: return@get call.respondNotFound()

        when (artifact) {
            is TextArtifact -> call.respondText(artifact.data())
            is JarArtifact -> call.respondBytes(artifact.data(), ContentType.parse("application/java-archive"))
            else -> call.respondNotFound()
        }
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(refresh: Boolean): Map<String, Artifact>? {
    val actionCoords = parameters.extractActionCoords(extractVersion = true)

    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    return if (refresh) {
        actionCoords.buildVersionArtifacts().also {
            bindingsCache.put(actionCoords, runCatching { it!! })
        }
    } else {
        bindingsCache.get(actionCoords) { runCatching { actionCoords.buildVersionArtifacts()!! } }.getOrNull()
    }
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
