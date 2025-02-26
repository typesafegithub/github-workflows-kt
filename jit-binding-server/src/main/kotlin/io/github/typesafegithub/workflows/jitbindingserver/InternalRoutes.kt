package io.github.typesafegithub.workflows.jitbindingserver

import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun Routing.internalRoutes(prometheusRegistry: PrometheusMeterRegistry) {
    get("/metrics") {
        call.respondText(text = prometheusRegistry.scrape())
    }

    get("/status") {
        call.respondText(text = "OK")
    }
}
