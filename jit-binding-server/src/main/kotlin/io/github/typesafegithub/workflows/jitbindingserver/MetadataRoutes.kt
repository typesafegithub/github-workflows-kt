package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.refreshAfterWrite
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrintWithoutVersion
import io.github.typesafegithub.workflows.mavenbinding.buildPackageArtifacts
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthToken
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.time.Duration.Companion.hours

private val logger = logger { }

typealias MetadataResult = Result<Map<String, String>>

private val metadataCache =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, MetadataResult> {
            runCatching {
                it.buildPackageArtifacts(githubAuthToken = getGithubAuthToken(), ::prefetchBindingArtifacts)
            }
        }

fun Routing.metadataRoutes(prometheusRegistry: PrometheusMeterRegistry) {
    CaffeineCacheMetrics.monitor(prometheusRegistry, metadataCache.underlying(), "metadata_cache")

    route("{owner}/{name}/{file}") {
        metadata()
    }

    route("/refresh/{owner}/{name}/{file}") {
        metadata(refresh = true)
    }
}

private fun Route.metadata(refresh: Boolean = false) {
    get {
        val actionCoords = call.parameters.extractActionCoords(extractVersion = false)

        logger.info { "➡️ Requesting metadata for ${actionCoords.prettyPrintWithoutVersion}" }

        if (refresh) {
            metadataCache.invalidate(actionCoords)
        }
        val metadataArtifacts = metadataCache.get(actionCoords).getOrThrow()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        if (file in metadataArtifacts) {
            when (val artifact = metadataArtifacts[file]) {
                is String -> call.respondText(text = artifact)
                else -> call.respondNotFound()
            }
        } else {
            call.respondNotFound()
        }
    }
}
