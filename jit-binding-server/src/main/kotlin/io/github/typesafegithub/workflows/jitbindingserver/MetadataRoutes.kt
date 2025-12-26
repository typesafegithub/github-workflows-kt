package io.github.typesafegithub.workflows.jitbindingserver

import com.sksamuel.aedile.core.LoadingCache
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrintWithoutVersion
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.route
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

private val logger = logger { }

typealias CachedMetadataArtifact = Map<String, String>

fun Routing.metadataRoutes(
    metadataCache: LoadingCache<ActionCoords, CachedMetadataArtifact>,
    prometheusRegistry: PrometheusMeterRegistry? = null,
) {
    prometheusRegistry?.let {
        CaffeineCacheMetrics.monitor(it, metadataCache.underlying(), "metadata_cache")
    }

    route("{owner}/{name}/{file}") {
        headMetadata(metadataCache)
        getMetadata(metadataCache)
    }

    route("/refresh/{owner}/{name}/{file}") {
        headMetadata(metadataCache, refresh = true)
        getMetadata(metadataCache, refresh = true)
    }
}

private fun Route.headMetadata(
    metadataCache: LoadingCache<ActionCoords, CachedMetadataArtifact>,
    refresh: Boolean = false,
) {
    head {
        val actionCoords = call.parameters.extractActionCoords(extractVersion = false)

        if (refresh) {
            metadataCache.invalidate(actionCoords)
        }
        val metadataArtifacts = metadataCache.get(actionCoords)

        if (refresh && !deliverOnRefreshRoute) return@head call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in metadataArtifacts) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }
    }
}

private fun Route.getMetadata(
    metadataCache: LoadingCache<ActionCoords, CachedMetadataArtifact>,
    refresh: Boolean = false,
) {
    get {
        val actionCoords = call.parameters.extractActionCoords(extractVersion = false)

        logger.info { "➡️ Requesting metadata for ${actionCoords.prettyPrintWithoutVersion}" }

        if (refresh) {
            metadataCache.invalidate(actionCoords)
        }
        val metadataArtifacts = metadataCache.get(actionCoords)

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
