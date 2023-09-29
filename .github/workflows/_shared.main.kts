#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.2.0")

import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.actions.SetupPythonV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.actions.jamesives.GithubPagesDeployActionV4
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.expr

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJavaV3(
            javaVersion = "11",
            distribution = SetupJavaV3.Distribution.Zulu,
        )
    )

fun JobBuilder<*>.setupPython() =
    uses(action = SetupPythonV4(pythonVersion = "3.8"))

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }

fun JobBuilder<*>.deployDocs() {
    run(command = "pip install -r docs/requirements.txt")

    val directoryToDeploy = "to-gh-pages"
    run(
        name = "Build Mkdocs docs",
        command = "mkdocs build --site-dir $directoryToDeploy",
    )
    uses(
        name = "Generate API docs",
        action = GradleBuildActionV2(
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
        action = GithubPagesDeployActionV4(
            folder = "$directoryToDeploy",
        ),
    )
}
