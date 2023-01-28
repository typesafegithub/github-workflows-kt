#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.35.0")
@file:Import("_shared.main.kts")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.jamesives.GithubPagesDeployActionV4
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

val directoryToDeploy = "to-gh-pages"

workflow(
    name = "Test docs",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "build_docs",
        name = "Build docs",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        setupPython()
        setupJava()
        run("pip install -r docs/requirements.txt")
        run("mkdocs build --site-dir $directoryToDeploy")
        uses(
            name = "Generate Dokka docs",
            action = GradleBuildActionV2(
                arguments = ":library:dokkaHtml",
            )
        )
        run("mkdir -p $directoryToDeploy/api-docs")
        run("cp -r library/build/dokka/html/* $directoryToDeploy/api-docs")
        run("find to-gh-pages")
        uses(GithubPagesDeployActionV4(
            folder = "$directoryToDeploy",
            branch = "test-docs-deployment",
        ))
    }
}.writeToFile()
