#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.15.0")

@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("gradle:actions__setup-gradle:v3")
@file:DependsOn("JamesIves:github-pages-deploy-action:v4")

import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.jamesives.GithubPagesDeployAction
import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.deployDocs() {
    run(command = "pip install -r docs/requirements.txt")

    val directoryToDeploy = "to-gh-pages"
    run(
        name = "Build Mkdocs docs",
        command = "mkdocs build --site-dir $directoryToDeploy",
    )
    uses(action = ActionsSetupGradle())
    run(
        name = "Generate API docs",
        command = "./gradlew :github-workflows-kt:dokkaHtml --no-configuration-cache",
    )
    run(
        name = "Prepare target directory for API docs",
        command = "mkdir -p $directoryToDeploy/api-docs",
    )
    run(
        name = "Copy Dokka output to Mkdocs output",
        command = "cp -r github-workflows-kt/build/dokka/html/* $directoryToDeploy/api-docs",
    )
    run(
        name = "Copy teaser image",
        command = "cp images/teaser-with-newest-version.svg $directoryToDeploy"
    )
    uses(
        name = "Deploy merged docs to GitHub Pages",
        action = GithubPagesDeployAction(
            folder = "$directoryToDeploy",
        ),
    )
}
