#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.39.0")
@file:Import("_shared.main.kts")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.jamesives.GithubPagesDeployActionV4
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
        setupJava()
        uses(
            name = "Build",
            action = GradleBuildActionV2(
                arguments = "build",
            )
        )

        setupPython()
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

        val directoryToDeploy = "to-gh-pages"
        run(
            name = "Build Mkdocs docs",
            command = "mkdocs build --site-dir $directoryToDeploy",
        )
        uses(
            name = "Generate API docs",
            action = GradleBuildActionV2(
                arguments = ":library:dokkaHtml",
            )
        )
        run(
            name = "Prepare target directory for API docs",
            command = "mkdir -p $directoryToDeploy/api-docs",
        )
        run(
            name = "Copy Dokka output to Mkdocs output",
            command = "cp -r library/build/dokka/html/* $directoryToDeploy/api-docs",
        )
        uses(
            name = "Deploy merged docs to GitHub Pages",
            GithubPagesDeployActionV4(
                folder = "$directoryToDeploy",
            )
        )
    }
}.writeToFile()
