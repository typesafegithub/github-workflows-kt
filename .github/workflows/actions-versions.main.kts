#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.3.2-20231027.055707-19")
//@file:Import("_shared.main.kts")
@file:Import("generated/peter-evans/create-issue-from-file.kt")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Cron
import io.github.typesafegithub.workflows.domain.triggers.Schedule
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Updates available",
    on = listOf(
        Schedule(listOf(
            Cron(dayWeek = "4", hour = "7", minute = "0")
        )),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
    yamlConsistencyJobCondition = expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }
    // Inlining temporarily
    //disableScheduledJobInForks,
) {
    job(
        id = "updates-available",
        runsOn = UbuntuLatest,
        condition = expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }
        // Inlining temporarily
        //disableScheduledJobInForks,
    ) {
        uses(action = CheckoutV4())
//        setupJava()
        // Inlining temporarily
        uses(
            name = "Set up JDK",
            action = SetupJavaV3(
                javaVersion = "11",
                distribution = SetupJavaV3.Distribution.Zulu,
            )
        )
        uses(
            name = "Run suggestVersions",
            env = linkedMapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
            action = GradleBuildActionV2(
                arguments = "suggestVersions",
            )
        )
        uses(
            name = "Create issue",
            action = CreateIssueFromFile(
                title = "New major versions of actions available",
                contentFilepath = "build/suggestVersions.md",
            )
        )
    }
}.writeToFile(generateActionBindings = true)
