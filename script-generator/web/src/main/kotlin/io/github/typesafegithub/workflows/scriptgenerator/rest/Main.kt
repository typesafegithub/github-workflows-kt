package io.github.typesafegithub.workflows.scriptgenerator.rest

import io.github.typesafegithub.workflows.scriptgenerator.yamlToKotlinScript
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }

        configureRouting()
    }.start(wait = true)
}

@Serializable
data class YamlToKotlinRequest(
    val yaml: String,
)

@Serializable
data class YamlToKotlinResponse(
    val kotlinScript: String,
)

private fun Application.configureRouting() {
    routing {
        route("api") {
            post("yaml-to-kotlin") {
                val requestBody = call.receive<YamlToKotlinRequest>()

                val kotlinScript = yamlToKotlinScript(requestBody.yaml)
                val response = YamlToKotlinResponse(
                    kotlinScript = kotlinScript,
                )

                call.respond(response)
            }
        }
    }
}
