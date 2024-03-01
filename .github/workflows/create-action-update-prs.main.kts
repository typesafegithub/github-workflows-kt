#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.12.0")
@file:Import("_shared.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("generated/actions/checkout.kt")
@file:Import("generated/gradle/gradle-build-action.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Cron
import io.github.typesafegithub.workflows.domain.triggers.Schedule
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

@OptIn(ExperimentalClientSideBindings::class)
workflow(
    name = "Create action update PRs",
    on = listOf(
        Schedule(listOf(
            Cron(dayWeek = "4", hour = "7", minute = "0")
        )),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
    yamlConsistencyJobCondition = disableScheduledJobInForks,
) {
    job(
        id = "check-updates-and-create-prs",
        runsOn = UbuntuLatest,
        condition = disableScheduledJobInForks,
    ) {
        uses(action = Checkout())
        setupJava()
        uses(
            name = "Run logic",
            env = linkedMapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
            action = GradleBuildAction(
                arguments = "createActionUpdatePRs",
            )
        )
    }
}.writeToFile(generateActionBindings = true)
