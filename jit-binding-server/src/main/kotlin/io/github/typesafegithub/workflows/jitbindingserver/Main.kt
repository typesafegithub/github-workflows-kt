package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.LoadingCache
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.refreshAfterWrite
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildPackageArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthToken
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
            buildVersionArtifacts = { buildVersionArtifacts(it.actionCoords, it.bindingVersion) },
            buildPackageArtifacts = ::buildPackageArtifacts,
            getGithubAuthToken = ::getGithubAuthToken,
        )
    }.start(wait = true)
}

private typealias VersionArtifactsBuilder = (CacheKey) -> VersionArtifacts?

fun Application.appModule(
    buildVersionArtifacts: VersionArtifactsBuilder,
    buildPackageArtifacts:
        suspend (
            ActionCoords,
            String,
            (Collection<ActionCoords>, BindingVersion) -> Unit,
            bindingVersion: BindingVersion,
        ) -> Map<String, String>,
    getGithubAuthToken: () -> String,
) {
    val bindingsCache = buildBindingsCache(buildVersionArtifacts)
    val metadataCache = buildMetadataCache(bindingsCache, buildPackageArtifacts, getGithubAuthToken)
    installPlugins(prometheusRegistry)

    routing {
        internalRoutes(prometheusRegistry)

        artifactRoutes(bindingsCache, prometheusRegistry)
        metadataRoutes(metadataCache, prometheusRegistry)
    }
}

typealias BindingsCache = LoadingCache<CacheKey, CachedVersionArtifact>

private fun buildBindingsCache(buildVersionArtifacts: VersionArtifactsBuilder): BindingsCache =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache { buildVersionArtifacts(it) }

typealias MetadataCache = LoadingCache<CacheKey, CachedMetadataArtifact>

private fun buildMetadataCache(
    bindingsCache: BindingsCache,
    buildPackageArtifacts:
        suspend (
            ActionCoords,
            String,
            (Collection<ActionCoords>, BindingVersion) -> Unit,
            bindingVersion: BindingVersion,
        ) -> Map<String, String>,
    getGithubAuthToken: () -> String,
): MetadataCache =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache {
            buildPackageArtifacts(
                it.actionCoords,
                getGithubAuthToken(),
                { coords, bindingsVersion ->
                    prefetchBindingArtifacts(coords, bindingsVersion, bindingsCache)
                },
                it.bindingVersion,
            )
        }

val deliverOnRefreshRoute = System.getenv("GWKT_DELIVER_ON_REFRESH").toBoolean()

suspend fun ApplicationCall.respondNotFound() = respondText(text = "Not found", status = HttpStatusCode.NotFound)

data class CacheKey(
    val actionCoords: ActionCoords,
    val bindingVersion: BindingVersion,
)
