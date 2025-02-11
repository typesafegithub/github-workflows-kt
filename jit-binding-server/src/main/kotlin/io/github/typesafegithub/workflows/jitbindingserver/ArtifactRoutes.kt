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
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.route
import kotlin.time.Duration.Companion.hours

private val logger = logger { }

typealias ArtifactResult = Result<Map<String, Artifact>>

val bindingsCache = Cache.Builder<ActionCoords, ArtifactResult>().expireAfterWrite(1.hours).build()

fun Routing.artifactRoutes() {
    route("{owner}/{name}/{version}/{file}") {
        artifact(refresh = false)
    }

    route("/refresh/{owner}/{name}/{version}/{file}") {
        artifact(refresh = true)
    }
}

private fun Route.artifact(refresh: Boolean = false) {
    headArtifact(refresh)
    getArtifact(refresh)
}

private fun Route.headArtifact(refresh: Boolean) {
    head {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@head call.respondNotFound()

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in bindingArtifacts) {
            call.respondText("Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }
    }
}

private fun Route.getArtifact(refresh: Boolean) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText("OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

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
