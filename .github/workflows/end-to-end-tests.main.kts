#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.13.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradleV3
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.net.URI
import java.net.ConnectException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

val GRADLE_ENCRYPTION_KEY by Contexts.secrets

@OptIn(ExperimentalKotlinLogicStep::class)
workflow(
    name = "End-to-end tests",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "test-jit-server",
        name = "Test Just-In-Time bindings server",
        runsOn = UbuntuLatest,
    ) {
        // Using bundled bindings to avoid generating them here.
        uses(action = CheckoutV4())
        uses(action = ActionsSetupGradleV3())

        run(
            name = "Start the server",
            command = "./gradlew :jit-binding-server:run &",
        )

        run(name = "Wait for the server to respond") {
            val timeSource = TimeSource.Monotonic
            val waitStart = timeSource.markNow()
            val timeout = 3.minutes

            while (timeSource.markNow() - waitStart < timeout) {
                try {
                    HttpClient.newHttpClient().send(
                        HttpRequest
                            .newBuilder(URI("http://0.0.0.0:8080/status"))
                            .GET()
                            .build(), BodyHandlers.ofString()
                    )
                    println("The server is alive!")
                    break
                } catch (_: ConnectException) {
                    Thread.sleep(5000)
                    println("The server is still starting...")
                }
            }
        }

        run(
            name = "Execute the script using the bindings from the server",
            command = """
                mv .github/workflows/test-script-consuming-jit-bindings.main.do-not-compile.kts .github/workflows/test-script-consuming-jit-bindings.main.kts
                .github/workflows/test-script-consuming-jit-bindings.main.kts
            """.trimIndent(),
        )
    }
}.writeToFile()
