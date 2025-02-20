package io.github.typesafegithub.workflows.jitbindingserver

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun Application.installPlugins(prometheusRegistry: PrometheusMeterRegistry) {
    install(CallId) {
        generate(15, "abcdefghijklmnopqrstuvwxyz0123456789")
        replyToHeader(HttpHeaders.XRequestId)
    }

    install(CallLogging) {
        callIdMdc("request-id")
    }

    install(MicrometerMetrics) {
        registry = prometheusRegistry
    }
}
