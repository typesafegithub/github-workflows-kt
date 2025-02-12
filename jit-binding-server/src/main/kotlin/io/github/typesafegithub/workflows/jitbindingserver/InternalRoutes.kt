package io.github.typesafegithub.workflows.jitbindingserver

import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.internalRoutes() {
    get("/status") {
        call.respondText("OK")
    }
}
