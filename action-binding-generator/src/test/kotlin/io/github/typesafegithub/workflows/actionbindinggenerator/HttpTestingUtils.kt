package io.github.typesafegithub.workflows.actionbindinggenerator

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond

fun mockClientReturning(response: String) = HttpClient(MockEngine { respond(content = response) })
