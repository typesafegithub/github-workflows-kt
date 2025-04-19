package io.github.typesafegithub.workflows.jitbindingserver

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.refreshAfterWrite
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.mavenbinding.Artifact
import io.github.typesafegithub.workflows.mavenbinding.JarArtifact
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.asFlow
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.util.UUID.randomUUID
import kotlin.time.Duration.Companion.hours

private const val METADATA_PARAMETER = "actionYaml"
private const val TYPES_PARAMETER = "types"

private val logger = logger { }

typealias ArtifactResult = Result<Map<String, Artifact>>

private val bindingsCache =
    Caffeine
        .newBuilder()
        .refreshAfterWrite(1.hours)
        .recordStats()
        .asLoadingCache<ActionCoords, ArtifactResult> {
            runCatching {
                it.buildVersionArtifacts(it.typesUuid?.let { "" })!!
            }
        }

fun Routing.artifactRoutes(prometheusRegistry: PrometheusMeterRegistry) {
    CaffeineCacheMetrics.monitor(prometheusRegistry, bindingsCache.underlying(), "bindings_cache")

    route("{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, refresh = false)
    }

    route("/refresh/{owner}/{name}/{version}/{file}") {
        artifact(prometheusRegistry, refresh = true)
    }
}

private fun Route.artifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean = false,
) {
    headArtifact(prometheusRegistry, refresh)
    getArtifact(prometheusRegistry, refresh)
    postArtifact(prometheusRegistry)
}

private fun Route.headArtifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean,
) {
    head {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@head call.respondNotFound()

        val file = call.parameters["file"] ?: return@head call.respondNotFound()

        if (file in bindingArtifacts) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondNotFound()
        }

        incrementArtifactCounter(prometheusRegistry, call)
    }
}

private fun Route.getArtifact(
    prometheusRegistry: PrometheusMeterRegistry,
    refresh: Boolean,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(refresh) ?: return@get call.respondNotFound()

        if (refresh && !deliverOnRefreshRoute) return@get call.respondText(text = "OK")

        val file = call.parameters["file"] ?: return@get call.respondNotFound()

        val artifact = bindingArtifacts[file] ?: return@get call.respondNotFound()

        when (artifact) {
            is TextArtifact -> call.respondText(text = artifact.data())
            is JarArtifact -> call.respondBytes(artifact.data(), ContentType.parse("application/java-archive"))
        }

        incrementArtifactCounter(prometheusRegistry, call)
    }
}

private fun Route.postArtifact(prometheusRegistry: PrometheusMeterRegistry) {
    post {
        val owner = "${call.parameters["owner"]}__types__${randomUUID()}"
        val name = call.parameters["name"]!!
        val version = call.parameters["version"]!!

        val (metadata, types) =
            runCatching {
                val parts =
                    call
                        .receiveMultipart()
                        .asFlow()
                        .map { it.name to it.asString() }
                        .toList()
                        .map { (name, result) ->
                            name to
                                when {
                                    result.isSuccess -> result.getOrThrow()
                                    else -> {
                                        call.respondText(
                                            text = HttpStatusCode.InternalServerError.description,
                                            status = HttpStatusCode.InternalServerError,
                                        )
                                        return@post
                                    }
                                }
                        }.associate { it }

                if (parts.keys.any { (it != METADATA_PARAMETER) && (it != TYPES_PARAMETER) }) {
                    call.respondText(
                        text = "Only '$METADATA_PARAMETER' and '$TYPES_PARAMETER' are allowed as form data fields",
                        status = HttpStatusCode.BadRequest,
                    )
                    return@post
                }
                if (!parts.containsKey(TYPES_PARAMETER)) {
                    call.respondText(
                        text = "'$TYPES_PARAMETER' field is mandatory",
                        status = HttpStatusCode.BadRequest,
                    )
                    return@post
                }
                parts[METADATA_PARAMETER] to parts[TYPES_PARAMETER]!!
            }.recover {
                null to call.receiveText()
            }.getOrThrow()

        if (!call.validateMetadata(metadata) || !call.validateTypes(types)) {
            return@post
        }

        call.toBindingArtifacts(refresh = true, owner = owner, types = types, metadata = metadata)
        call.respondText(text = "$owner:$name:$version")

        incrementArtifactCounter(prometheusRegistry, call)
    }
}

private suspend fun PartData.asString() =
    runCatching {
        when (this) {
            is PartData.FileItem -> provider().readRemaining().readText()
            is PartData.FormItem -> value
            else -> {
                logger.error { "Unexpected part data ${this::class.simpleName}" }
                error("Unexpected part data ${this::class.simpleName}")
            }
        }
    }

private suspend fun ApplicationCall.validateTypes(types: String): Boolean {
    runCatching {
        Load().loadOne(types)
    }.onFailure {
        respondText(
            text = "Exception while parsing supplied $TYPES_PARAMETER:\n${it.stackTraceToString()}",
            status = HttpStatusCode.UnprocessableEntity,
        )
        return false
    }
    return true
}

private suspend fun ApplicationCall.validateMetadata(metadata: String?): Boolean {
    var result = true
    if (metadata != null) {
        if (metadata.isEmpty()) {
            respondText(
                text = "Supplied $METADATA_PARAMETER is empty",
                status = HttpStatusCode.UnprocessableEntity,
            )
            result = false
        }

        runCatching {
            Load().loadOne(metadata)
        }.onFailure {
            respondText(
                text = "Exception while parsing supplied $METADATA_PARAMETER:\n${it.stackTraceToString()}",
                status = HttpStatusCode.UnprocessableEntity,
            )
            result = false
        }
    }
    return result
}

private suspend fun ApplicationCall.toBindingArtifacts(
    refresh: Boolean,
    owner: String = parameters["owner"]!!,
    types: String? = null,
    metadata: String? = null,
): Map<String, Artifact>? {
    val actionCoords = parameters.extractActionCoords(extractVersion = true, owner = owner)

    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    if (refresh) {
        bindingsCache.invalidate(actionCoords)
    }
    return (
        if (types != null) {
            bindingsCache.get(actionCoords) { runCatching { actionCoords.buildVersionArtifacts(types, metadata)!! } }
        } else {
            bindingsCache.get(actionCoords)
        }
    ).getOrNull()
}

private fun incrementArtifactCounter(
    prometheusRegistry: PrometheusMeterRegistry,
    call: ApplicationCall,
) {
    val owner = call.parameters["owner"] ?: "unknown"
    val name = call.parameters["name"] ?: "unknown"
    val version = call.parameters["version"] ?: "unknown"
    val file = call.parameters["file"] ?: "unknown"
    val method = call.request.httpMethod.value
    val status =
        call.response
            .status()
            ?.value
            ?.toString() ?: "unknown"

    val counter =
        prometheusRegistry.counter(
            "artifact_requests_total",
            listOf(
                Tag.of("owner", owner),
                Tag.of("name", name),
                Tag.of("version", version),
                Tag.of("file", file),
                Tag.of("method", method),
                Tag.of("status", status),
            ),
        )
    counter.increment()
}
