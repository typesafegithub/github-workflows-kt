#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:0.48.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.gradle.WrapperValidationActionV1
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
        uses(action = CheckoutV3())
        uses(
            name = "Validate wrapper",
            action = WrapperValidationActionV1(),
        )
    }
}.writeToFile()
