#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:2.3.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")

@file:Import("setup-java.main.kts")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2022
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow

workflow(
    name = "Check if generated code up to date",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__,
) {
    listOf(UbuntuLatest, Windows2022).forEach { runnerType ->
        job(
            id = "check-on-${runnerType::class.simpleName}",
            runsOn = runnerType,
        ) {
            uses(action = Checkout())
            setupJava()
            uses(action = ActionsSetupGradle())
            run(
                name = "Generate action bindings",
                command = "./gradlew :automation:code-generator:run",
            )
            run(
                name = "Fail if there are any changes in the generated code or docs",
                command = "git diff --exit-code github-workflows-kt/src/gen/ docs/supported-actions.md"
            )
        }
    }
}
