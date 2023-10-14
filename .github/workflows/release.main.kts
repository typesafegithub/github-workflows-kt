#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.3.0")
@file:Import("_shared.main.kts")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

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
        uses(action = CheckoutV4())
        setupJava()
        uses(
            name = "Build",
            action = GradleBuildActionV2(
                arguments = "build",
            )
        )

        setupPython()

        // From here, there are steps performing deployments. Before, it's only about building and testing.

        val libraries = listOf(
            // The order is intended here - the library will once depend on the generator,
            // so the generator needs to go first.
            ":automation:action-binding-generator",
            ":library",
        )

        libraries.forEach { library ->
            uses(
                name = "Publish '$library' to Sonatype",
                action = GradleBuildActionV2(
                    arguments = "$library:publishToSonatype closeAndReleaseSonatypeStagingRepository",
                ),
            )
        }

        libraries.forEach { library ->
            uses(
                name = "Wait until '$library' present in Maven Central",
                action = GradleBuildActionV2(
                    arguments = "$library:waitUntilLibraryPresentInMavenCentral",
                ),
            )
        }

        deployDocs()
    }
}.writeToFile()
