package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import java.time.Duration

private val prometheusRegistry =
    PrometheusMeterRegistry(
        object : PrometheusConfig {
            override fun get(key: String): String? = null

            override fun prefix(): String = "github-actions-binding-server"

            override fun step() = Duration.ofSeconds(10)
        },
    )
private val logger =
    System
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * !! IMPORTANT:                                                                       !!
         * !!     This statement has to always be executed first before **any** other code,    !!
         * !!     or else the property "java.util.logging.manager" may have no effect anymore! !!
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        .setProperty("java.util.logging.manager", org.apache.logging.log4j.jul.LogManager::class.java.name)
        .let { logger { } }

fun main() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error(throwable) { "Uncaught exception in thread $thread" }
    }
    embeddedServer(Netty, port = 8080) {
        installPlugins(prometheusRegistry)

        routing {
            internalRoutes(prometheusRegistry)

            artifactRoutes()
            metadataRoutes()
        }
    }.start(wait = true)
}

val deliverOnRefreshRoute = System.getenv("GWKT_DELIVER_ON_REFRESH").toBoolean()

suspend fun ApplicationCall.respondNotFound() = respondText("Not found", status = HttpStatusCode.NotFound)
