package io.github.typesafegithub.workflows.jitbindingserver

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.opentelemetry.instrumentation.ktor.v3_0.server.KtorServerTracing

fun Application.installPlugins() {
    install(CallId) {
        generate(15, "abcdefghijklmnopqrstuvwxyz0123456789")
        replyToHeader(HttpHeaders.XRequestId)
    }

    install(CallLogging) {
        callIdMdc("request-id")
    }

    install(KtorServerTracing) {
        setOpenTelemetry(buildOpenTelemetryConfig("github-actions-bindings"))
    }
}
