#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v5")
@file:DependsOn("gradle:wrapper-validation-action:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.WrapperValidationAction
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow

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
    sourceFile = __FILE__,
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
}
