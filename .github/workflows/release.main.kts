#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.25.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3.Distribution.Zulu
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

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
        uses(CheckoutV3())
        uses(
            name = "Set up JDK",
            action = SetupJavaV3(
                javaVersion = "17",
                distribution = Zulu,
            )
        )
        uses(
            name = "Build",
            action = GradleBuildActionV2(
                arguments = "build",
            )
        )

        uses(SetupPythonV4(pythonVersion = "3.8"))
        run("pip install -r docs/requirements.txt")

        // From here, there are steps performing deployments. Before, it's only about building and testing.

        uses(
            name = "Publish to Sonatype",
            action = GradleBuildActionV2(
                arguments = ":library:publishToSonatype closeAndReleaseSonatypeStagingRepository",
            )
        )
        uses(
            name = "Wait until library present in Maven Central",
            action = GradleBuildActionV2(
                arguments = ":library:waitUntilLibraryPresentInMavenCentral",
            )
        )
        run(
            name = "Deploy docs",
            command = "mkdocs gh-deploy --force",
        )
    }
}.writeToFile()
