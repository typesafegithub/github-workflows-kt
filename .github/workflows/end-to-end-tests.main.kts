#!/usr/bin/env kotlin
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.14.1-SNAPSHOT")
@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "End-to-end tests",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    yamlConsistencyJobAdditionalSteps = {
        uses(
            name = "Set up JDK",
            action = SetupJava(
                javaVersion = "11",
                distribution = SetupJava.Distribution.Zulu,
            ),
        )
        uses(action = ActionsSetupGradle(generateJobSummary = false))
        run(
            name = "Publish to Maven local",
            command = "./gradlew publishToMavenLocal",
        )
    },
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "main-job",
        name = "Main job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(action = Checkout())
        run(command = "echo 'Hello world!'")
    }
}.writeToFile()
