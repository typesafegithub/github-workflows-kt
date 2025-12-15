package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.LoadingCache
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.refreshAfterWrite
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildPackageArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import java.time.Duration
import kotlin.time.Duration.Companion.hours

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

private val prometheusRegistry =
    PrometheusMeterRegistry(
        object : PrometheusConfig {
            override fun get(key: String): String? = null

            override fun prefix(): String = "github-actions-binding-server"

            @Suppress("MagicNumber")
            override fun step() = Duration.ofSeconds(10)
        },
    )

fun main() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error(throwable) { "Uncaught exception in thread $thread" }
    }
    embeddedServer(Netty, port = 8080) {
        appModule(
            buildVersionArtifacts = ::buildVersionArtifacts,
            buildPackageArtifacts = ::buildPackageArtifacts,
            getGithubAuthToken = ::getGithubAuthToken,
        )
    }.start(wait = true)
}

fun Application.appModule(
    buildVersionArtifacts: suspend (ActionCoords, HttpClient) -> VersionArtifacts?,
    buildPackageArtifacts: suspend (ActionCoords, String, (Collection<ActionCoords>) -> Unit) -> Map<String, String>,
    getGithubAuthToken: () -> String,
) {
    val httpClient = HttpClient(CIO)
    val bindingsCache = buildBindingsCache(buildVersionArtifacts, httpClient)
    val metadataCache = buildMetadataCache(bindingsCache, buildPackageArtifacts, getGithubAuthToken)
    installPlugins(prometheusRegistry)

    routing {
        internalRoutes(prometheusRegistry)

        artifactRoutes(bindingsCache, prometheusRegistry)
        metadataRoutes(metadataCache, prometheusRegistry)
    }
}

private fun buildBindingsCache(
    buildVersionArtifacts: suspend (ActionCoords, HttpClient) -> VersionArtifacts?,
    httpClient: HttpClient,
): LoadingCache<ActionCoords, CachedVersionArtifact> =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, CachedVersionArtifact> { buildVersionArtifacts(it, httpClient) }

@Suppress("ktlint:standard:function-signature") // Conflict with detekt.
private fun buildMetadataCache(
    bindingsCache: LoadingCache<ActionCoords, CachedVersionArtifact>,
    buildPackageArtifacts: suspend (ActionCoords, String, (Collection<ActionCoords>) -> Unit) -> Map<String, String>,
    getGithubAuthToken: () -> String,
): LoadingCache<ActionCoords, CachedMetadataArtifact> =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, CachedMetadataArtifact> {
            buildPackageArtifacts(
                it,
                getGithubAuthToken(),
                { coords -> prefetchBindingArtifacts(coords, bindingsCache) },
            )
        }

val deliverOnRefreshRoute = System.getenv("GWKT_DELIVER_ON_REFRESH").toBoolean()

suspend fun ApplicationCall.respondNotFound() = respondText(text = "Not found", status = HttpStatusCode.NotFound)
