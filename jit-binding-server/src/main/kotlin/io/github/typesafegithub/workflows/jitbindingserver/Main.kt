package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.mavenbinding.buildJar
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/binding/{owner}/{name}/{version}") {
                val owner = call.parameters["owner"]!!
                val name = call.parameters["name"]!!
                val version = call.parameters["version"]!!
                call.respondOutputStream(
                    contentType = ContentType.parse("application/java-archive"),
                    status = HttpStatusCode.OK,
                    producer = { this.buildJar(owner = owner, name = name, version = version) },
                )
            }
        }
    }.start(wait = true)
}
