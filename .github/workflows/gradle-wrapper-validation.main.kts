#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.32.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradle.WrapperValidationActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

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
        uses(CheckoutV3())
        uses(
            name = "Validate wrapper",
            action = WrapperValidationActionV1(),
        )
    }
}.writeToFile()
