#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.5.0")
@file:Import("generated/gradle/gradle-build-action.kt")
@file:Import("generated/JamesIves/github-pages-deploy-action.kt")

import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.deployDocs() {
    run(command = "pip install -r docs/requirements.txt")

    val directoryToDeploy = "to-gh-pages"
    run(
        name = "Build Mkdocs docs",
        command = "mkdocs build --site-dir $directoryToDeploy",
    )
    uses(
        name = "Generate API docs",
        action = GradleBuildAction(
            arguments = ":library:dokkaHtml",
        ),
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
        action = GithubPagesDeployAction(
            folder = "$directoryToDeploy",
        ),
    )
}
