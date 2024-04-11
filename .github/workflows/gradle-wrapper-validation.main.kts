#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.14.0")

@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:wrapper-validation-action:v2")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.WrapperValidationAction
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Validate Gradle wrapper",
    on = listOf(
        Push(
            branches = listOf("main"),
            paths = listOf("gradle/wrapper/gradle-wrapper.jar"),
        ),
        PullRequest(
            paths = listOf("gradle/wrapper/gradle-wrapper.jar"),
        ),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "validation",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        uses(
            name = "Validate wrapper",
            action = WrapperValidationAction(),
        )
    }
}.writeToFile()
