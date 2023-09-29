#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.2.0")
@file:Import("_shared.main.kts")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2022
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Check if action bindings up to date",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    listOf(UbuntuLatest, Windows2022).forEach { runnerType ->
        job(
            id = "check-on-${runnerType::class.simpleName}",
            runsOn = runnerType,
        ) {
            uses(action = CheckoutV4())
            setupJava()
            uses(
                name = "Generate action bindings",
                action = GradleBuildActionV2(
                    arguments = ":automation:code-generator:run",
                )
            )
            run(
                name = "Fail if there are any changes in the generated action bindings or their list in the docs",
                command = "git diff --exit-code library/src/gen/ docs/supported-actions.md"
            )
        }
    }
}.writeToFile()
