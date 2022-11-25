#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.32.0")
@file:Import("_shared.main.kts")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

workflow(
    name = "Check if wrappers up to date",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
    yamlConsistencyJobCondition = disableScheduledJobInForks,
) {
    job(
        id = "check",
        runsOn = UbuntuLatest,
        condition = disableScheduledJobInForks,
    ) {
        uses(CheckoutV3())
        setupJava()
        uses(
            name = "Generate wrappers",
            action = GradleBuildActionV2(
                arguments = ":wrapper-generator:run",
            )
        )
        run(
            name = "Fail if there are any changes in the generated wrappers or their list in the docs",
            command = "git diff --exit-code library/src/gen/ docs/supported-actions.md"
        )
    }
}.writeToFile()
