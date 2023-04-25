package io.github.typesafegithub.workflows.scriptgenerator.rest

import io.github.typesafegithub.workflows.scriptgenerator.rest.api.YamlToKotlinRequest
import io.github.typesafegithub.workflows.scriptgenerator.rest.api.YamlToKotlinResponse
import io.github.typesafegithub.workflows.scriptgenerator.yamlToKotlinScript
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }

        configureRouting()
    }.start(wait = true)
}

private fun Application.configureRouting() {
    routing {
        route("api") {
            post("yaml-to-kotlin") {
                val requestBody = call.receive<YamlToKotlinRequest>()

                val kotlinScript = try {
                    yamlToKotlinScript(requestBody.yaml)
                } catch (e: Throwable) {
                    val response = YamlToKotlinResponse(
                        error = e.message,
                    )
                    call.respond(status = HttpStatusCode.BadRequest, message = response)
                    return@post
                }
                val response = YamlToKotlinResponse(
                    kotlinScript = kotlinScript,
                )

                call.respond(response)
            }
        }

        singlePageApplication {
            useResources = true
        }
    }
}
