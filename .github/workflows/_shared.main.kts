#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.4.1-20231029.193015-11")
@file:Import("generated/actions/setup-java.kt")
@file:Import("generated/actions/setup-python.kt")
@file:Import("generated/gradle/gradle-build-action.kt")
@file:Import("generated/JamesIves/github-pages-deploy-action.kt")

import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.expr

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJava(
            javaVersion = "11",
            distribution = SetupJava.Distribution.Zulu,
        )
    )

fun JobBuilder<*>.setupPython() =
    uses(action = SetupPython(pythonVersion = "3.8"))

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

val libraries = listOf(
    ":library",
    ":automation:action-binding-generator",
)
