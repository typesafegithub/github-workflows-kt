#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.7.0")
@file:Import("setup-java.main.kts")
@file:Import("generated/actions/checkout.kt")
@file:Import("generated/gradle/gradle-build-action.kt")

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
            uses(action = Checkout())
            setupJava()
            uses(
                name = "Generate action bindings",
                action = GradleBuildAction(
                    arguments = ":automation:code-generator:run",
                )
            )
            run(
                name = "Fail if there are any changes in the generated action bindings or their list in the docs",
                command = "git diff --exit-code github-workflows-kt/src/gen/ docs/supported-actions.md"
            )
        }
    }
}.writeToFile(generateActionBindings = true)
