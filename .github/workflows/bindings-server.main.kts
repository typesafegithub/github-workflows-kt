#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.0.1")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__setup-gradle:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.*
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import java.net.URI
import java.net.ConnectException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

val DOCKERHUB_USERNAME by Contexts.secrets
val DOCKERHUB_PASSWORD by Contexts.secrets
val TRIGGER_IMAGE_PULL by Contexts.secrets
val GITHUB_TOKEN by Contexts.secrets

@OptIn(ExperimentalKotlinLogicStep::class)
workflow(
    name = "Bindings server",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
        Schedule(triggers = listOf(Cron(minute = "0", hour = "0", dayWeek = "SUN"))),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__,
) {
    val endToEndTest = job(
        id = "end-to-end-test",
        name = "End-to-end test",
        runsOn = UbuntuLatest,
        env = mapOf(
            "GITHUB_TOKEN" to expr { GITHUB_TOKEN },
        ),
    ) {
        uses(action = Checkout())
        uses(action = ActionsSetupGradle())

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

        cleanMavenLocal()

        run(
            name = "Execute the script using the bindings from the server",
            command = """
                mv .github/workflows/test-script-consuming-jit-bindings.main.do-not-compile.kts .github/workflows/test-script-consuming-jit-bindings.main.kts
                .github/workflows/test-script-consuming-jit-bindings.main.kts
            """.trimIndent(),
        )

        cleanMavenLocal()

        run(
            name = "Execute the script using bindings but without dependency on library",
            command = """
                mv .github/workflows/test-served-bindings-depend-on-library.main.do-not-compile.kts .github/workflows/test-served-bindings-depend-on-library.main.kts
                .github/workflows/test-served-bindings-depend-on-library.main.kts
            """.trimIndent(),
        )

        cleanMavenLocal()

        run(
            name = "Compile a Gradle project using the bindings from the server",
            command = """
                cd .github/workflows/test-gradle-project-using-bindings-server
                ./gradlew build
            """.trimIndent(),
        )

        run(
            name = "Fetch maven-metadata.xml for top-level action",
            command = "curl --fail http://localhost:8080/actions/checkout/maven-metadata.xml | grep '<version>v4</version>'",
        )
        run(
            name = "Fetch maven-metadata.xml for nested action",
            command = "curl --fail http://localhost:8080/actions/cache__save/maven-metadata.xml | grep '<version>v4</version>'",
        )
    }

    val testWithOlderKotlinAsConsumer = job(
        id = "test-with-older-kotlin-as-consumer",
        name = "Test with older Kotlin as consumer",
        runsOn = UbuntuLatest,
    ) {
        val version = "1.8.0"
        run(
            name = "Download older Kotlin compiler",
            command = "curl -o kotlin-compiler-$version.zip https://github.com/JetBrains/kotlin/releases/download/v$version/kotlin-compiler-$version.zip",
        )
        run(
            name = "Unzip",
            command = "unzip kotlin-compiler-$version.zip -d kotlin-compiler-$version",
        )
    }

    job(
        id = "deploy",
        name = "Deploy to DockerHub",
        runsOn = UbuntuLatest,
        `if` = expr { "${github.event_name} == 'workflow_dispatch' || ${github.event_name} == 'schedule'" },
        needs = listOf(endToEndTest, testWithOlderKotlinAsConsumer),
        env = mapOf(
            "DOCKERHUB_USERNAME" to expr { DOCKERHUB_USERNAME },
            "DOCKERHUB_PASSWORD" to expr { DOCKERHUB_PASSWORD },
        ),
        environment = Environment(name = "DockerHub"),
    ) {
        uses(action = Checkout())
        uses(action = ActionsSetupGradle())
        run(
            name = "Build and publish image",
            command = "./gradlew :jit-binding-server:publishImage",
        )
        run(
            name = "Use newest image on the server",
            command = "curl -X POST ${expr { TRIGGER_IMAGE_PULL }} --insecure",
        )
    }
}

fun JobBuilder<JobOutputs.EMPTY>.cleanMavenLocal() {
    run(
        name = "Clean Maven Local to fetch required POMs again",
        command = "rm -rf ~/.m2/repository/"
    )
}
