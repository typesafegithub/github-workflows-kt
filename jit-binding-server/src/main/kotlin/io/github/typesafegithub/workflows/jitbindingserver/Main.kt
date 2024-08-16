package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.reactivecircus.cache4k.Cache
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.mavenbinding.Artifact
import io.github.typesafegithub.workflows.mavenbinding.JarArtifact
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.github.typesafegithub.workflows.mavenbinding.buildPackageArtifacts
import io.github.typesafegithub.workflows.mavenbinding.buildVersionArtifacts
import io.github.typesafegithub.workflows.shared.internal.getGithubToken
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.XRequestId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.opentelemetry.instrumentation.ktor.v3_0.server.KtorServerTracing
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import java.util.UUID.randomUUID
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

fun main() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error(throwable) { "Uncaught exception in thread $thread" }
    }

    val bindingsCache =
        Cache
            .Builder<ActionCoords, Result<Map<String, Artifact>>>()
            .expireAfterWrite(1.hours)
            .build()
    val openTelemetry = buildOpenTelemetryConfig(serviceName = "github-actions-bindings")

    embeddedServer(Netty, port = 8080) {
        install(CallId) {
            generate(
                length = 15,
                dictionary = "abcdefghijklmnopqrstuvwxyz0123456789",
            )
            replyToHeader(XRequestId)
        }
        install(CallLogging) {
            callIdMdc("request-id")
        }
        install(KtorServerTracing) {
            setOpenTelemetry(openTelemetry)
        }
        routing {
            route("{owner}/{name}/{version}/{file}") {
                artifact(bindingsCache)
            }

            route("{owner}/{name}/{file}") {
                metadata()
            }

            route("/refresh") {
                route("{owner}/{name}/{version}/{file}") {
                    artifact(bindingsCache, refresh = true)
                }

                route("{owner}/{name}/{file}") {
                    metadata(refresh = true)
                }
            }

            get("/status") {
                call.respondText(text = "OK")
            }
        }
    }.start(wait = true)
}

private fun Route.metadata(refresh: Boolean = false) {
    get {
        if (refresh && !deliverOnRefreshRoute) {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            return@get
        }

        val owner = call.parameters["owner"]!!
        val nameAndPath = call.parameters["name"]!!.split("__")
        val name = nameAndPath.first()
        val file = call.parameters["file"]!!
        val actionCoords =
            ActionCoords(
                owner = owner,
                name = name,
                version = "irrelevant",
                path = nameAndPath.drop(1).joinToString("/").takeUnless { it.isBlank() },
            )
        val bindingArtifacts = actionCoords.buildPackageArtifacts(githubToken = getGithubToken())
        if (file in bindingArtifacts) {
            when (val artifact = bindingArtifacts[file]) {
                is String -> call.respondText(text = artifact)
                else -> call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            }
        } else {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
        }
    }
}

private fun Route.artifact(
    bindingsCache: Cache<ActionCoords, Result<Map<String, Artifact>>>,
    refresh: Boolean = false,
) {
    get {
        val bindingArtifacts = call.toBindingArtifacts(bindingsCache, refresh)
        if (bindingArtifacts == null) {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            return@get
        } else if (refresh && !deliverOnRefreshRoute) {
            call.respondText(text = "OK")
            return@get
        }

        val file = call.parameters["file"]!!
        if (file in bindingArtifacts) {
            when (val artifact = bindingArtifacts[file]) {
                is TextArtifact -> call.respondText(text = artifact.data())
                is JarArtifact ->
                    call.respondBytes(
                        bytes = artifact.data(),
                        contentType = ContentType.parse("application/java-archive"),
                    )

                else -> call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            }
        } else {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
        }
    }

    head {
        val bindingArtifacts = call.toBindingArtifacts(bindingsCache, refresh)
        val file = call.parameters["file"]!!
        if (bindingArtifacts == null) {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
            return@head
        }
        if (file in bindingArtifacts) {
            call.respondText(text = "Exists", status = HttpStatusCode.OK)
        } else {
            call.respondText(text = "Not found", status = HttpStatusCode.NotFound)
        }
    }

    post {
        val owner = "${call.parameters["owner"]}__types__${randomUUID()}"
        val name = call.parameters["name"]!!
        val version = call.parameters["version"]!!
        val types = call.receiveText()
        runCatching {
            Load().loadOne(types)
        }.onFailure {
            call.respondText(
                text = "Exception while parsing supplied typings:\n${it.stackTraceToString()}",
                status = HttpStatusCode.UnprocessableEntity,
            )
            return@post
        }
        call.toBindingArtifacts(bindingsCache, refresh = true, owner = owner, types = types)
        call.respondText(text = "$owner:$name:$version")
    }
}

private suspend fun ApplicationCall.toBindingArtifacts(
    bindingsCache: Cache<ActionCoords, Result<Map<String, Artifact>>>,
    refresh: Boolean,
    owner: String = parameters["owner"]!!,
    types: String? = null,
): Map<String, Artifact>? {
    val nameAndPath = parameters["name"]!!.split("__")
    val name = nameAndPath.first()
    val version = parameters["version"]!!
    // we cannot give the types UUID separately from the post handler
    // only in the post handler we generate the UUID, but for the other
    // handlers the UUID part is already coming through the request as part of the owner
    val ownerAndTypesUuid = owner.split("__types__", limit = 2)
    val ownerPlain = ownerAndTypesUuid.first()
    val typesUuid =
        ownerAndTypesUuid
            .drop(1)
            .takeIf { it.isNotEmpty() }
            ?.single()
    val actionCoords =
        ActionCoords(
            owner = ownerPlain,
            name = name,
            version = version,
            path = nameAndPath.drop(1).joinToString("/").takeUnless { it.isBlank() },
            typesUuid = typesUuid,
        )
    logger.info { "➡️ Requesting ${actionCoords.prettyPrint}" }
    val bindingArtifacts =
        if (refresh) {
            actionCoords.buildVersionArtifacts(types ?: typesUuid?.let { "" }).also {
                bindingsCache.put(actionCoords, Result.of(it))
            }
        } else {
            bindingsCache
                .get(actionCoords) { Result.of(actionCoords.buildVersionArtifacts(types ?: typesUuid?.let { "" })) }
                .getOrNull()
        }
    return bindingArtifacts
}

private fun Result.Companion.failure(): Result<Nothing> = failure(object : Throwable() {})

private fun <T> Result.Companion.of(value: T?): Result<T> = value?.let { success(it) } ?: failure()

private val deliverOnRefreshRoute = System.getenv("GWKT_DELIVER_ON_REFRESH").toBoolean()
