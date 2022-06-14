#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.19.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradleupdate.UpdateGradleWrapperActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

workflow(
    name = "Update Gradle Wrapper",
    on = listOf(
        Schedule(listOf(Cron("0 0 * * *"))), // Daily, at midnight.
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "update-gradle-wrapper",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        uses(UpdateGradleWrapperActionV1())
    }
}.writeToFile()
