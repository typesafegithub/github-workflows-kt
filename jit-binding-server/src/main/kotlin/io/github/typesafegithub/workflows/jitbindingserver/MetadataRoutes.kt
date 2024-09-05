package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrintWithoutVersion
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

private val logger = logger { }

typealias CachedMetadataArtifact = Map<String, String>

fun Routing.metadataRoutes(
    metadataCache: MetadataCache,
    prometheusRegistry: PrometheusMeterRegistry? = null,
) {
    prometheusRegistry?.let {
        CaffeineCacheMetrics.monitor(it, metadataCache.underlying(), "metadata_cache")
    }

    route("{owner}/{name}/{file}") {
        metadata(metadataCache)
    }

    route("/refresh/{owner}/{name}/{file}") {
        metadata(metadataCache, refresh = true)
    }

    route("""(?<bindingVersion>v\d+)""".toRegex()) {
        route("{owner}/{name}/{file}") {
            metadata(metadataCache)
        }

        route("/refresh/{owner}/{name}/{file}") {
            metadata(metadataCache, refresh = true)
        }
    }
}

private fun Route.metadata(
    metadataCache: MetadataCache,
    refresh: Boolean = false,
) {
    get {
        val bindingVersion = call.bindingVersion ?: return@get call.respondNotFound()
        val actionCoords = call.parameters.extractActionCoords(extractVersion = false)

        logger.info {
            "➡️ Requesting metadata for ${actionCoords.prettyPrintWithoutVersion} binding version $bindingVersion"
        }
        val cacheKey = CacheKey(actionCoords, bindingVersion)
        if (refresh) {
            metadataCache.invalidate(cacheKey)
        }
        val metadataArtifacts = metadataCache.get(cacheKey)

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
