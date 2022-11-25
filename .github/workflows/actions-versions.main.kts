#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.32.0")
@file:Import("_shared.main.kts")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.peterevans.CreateIssueFromFileV4
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

workflow(
    name = "Updates available",
    on = listOf(
        Schedule(listOf(
            Cron(hour = "7", minute = "0")
        )),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
    yamlConsistencyJobCondition = disableScheduledJobInForks,
) {
    job(
        id = "updates-available",
        runsOn = UbuntuLatest,
        condition = disableScheduledJobInForks,
    ) {
        uses(CheckoutV3())
        setupJava()
        uses(
            name = "Run suggestVersions",
            env = linkedMapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
            action = GradleBuildActionV2(
                arguments = "suggestVersions",
            )
        )
        uses(
            name = "Create issue",
            action = CreateIssueFromFileV4(
                title = "Updates available",
                contentFilepath = "build/suggestVersions.md",
            )
        )
    }
}.writeToFile()
