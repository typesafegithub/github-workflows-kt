#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.12.0")
@file:Import("_shared.main.kts")
@file:Import("release-common.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")
@file:Import("generated/actions/checkout.kt")
@file:Import("generated/gradle/actions/setup-gradle.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

@OptIn(ExperimentalClientSideBindings::class)
workflow(
    name = "Release",
    on = listOf(Push(tags = listOf("v*.*.*"))),
    sourceFile = __FILE__.toPath(),
    env = linkedMapOf(
        "SIGNING_KEY" to expr("secrets.SIGNING_KEY"),
        "SIGNING_PASSWORD" to expr("secrets.SIGNING_PASSWORD"),
        "ORG_GRADLE_PROJECT_sonatypeUsername" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEUSERNAME"),
        "ORG_GRADLE_PROJECT_sonatypePassword" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEPASSWORD"),
    ),
) {
    job(
        id = "release",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        setupJava()
        uses(action = ActionsSetupGradle())
        run(
            name = "Build",
            command = "./gradlew build",
        )

        setupPython()

        // From here, there are steps performing deployments. Before, it's only about building and testing.

        libraries.forEach { library ->
            run(
                name = "Publish '$library' to Sonatype",
                command = "./gradlew $library:publishToSonatype closeAndReleaseSonatypeStagingRepository --no-configuration-cache",
            )
        }

        libraries.forEach { library ->
            run(
                name = "Wait until '$library' present in Maven Central",
                command = "./gradlew $library:waitUntilLibraryPresentInMavenCentral",
            )
        }

        deployDocs()
    }
}.writeToFile(generateActionBindings = true)
