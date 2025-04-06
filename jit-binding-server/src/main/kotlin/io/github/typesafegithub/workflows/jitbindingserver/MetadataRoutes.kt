package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.mavenbinding.buildPackageArtifacts
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Routing.metadataRoutes() {
    route("{owner}/{name}/{file}") {
        metadata()
    }

    route("/refresh/{owner}/{name}/{file}") {
        metadata(refresh = true)
    }
}

private fun Route.metadata(refresh: Boolean = false) {
    get {
        if (refresh && !deliverOnRefreshRoute) return@get call.respondNotFound()

        val file = call.parameters["file"] ?: return@get call.respondNotFound()
        val actionCoords = call.parameters.extractActionCoords(extractVersion = false)

        val (githubAuthToken, tokenType) = getGithubAuthToken()
        // TODO: use tokenType to register a metric on the fallback
        // TODO: what if getGithubAuthToken() returns null? Looks like destructuring asserts non-null?
        val bindingArtifacts = actionCoords.buildPackageArtifacts(githubAuthToken = githubAuthToken)
        if (file in bindingArtifacts) {
            when (val artifact = bindingArtifacts[file]) {
                is String -> call.respondText(text = artifact)
                else -> call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            }
        } else {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
        }
    }
}
