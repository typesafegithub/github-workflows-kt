#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.4.0")
@file:Import("_shared.main.kts")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2022
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "testing",
        runsOn = UbuntuLatest,
    ) {
        uses(action = CheckoutV4())
        setupJava()
        val checkIfSnapshot = uses(
            name = "check-if-snapshot",
            action = GradleBuildActionV2(
                arguments = "setIsSnapshotFlagInGithubOutput",
            ),
        )
        run(
            name = "check-if-snapshot-2",
            command = """
                echo "is-snapshot-2=true" >> ${'$'}GITHUB_OUTPUT
            """.trimIndent()
        )
        run(
            command = """
                echo 'is-snapshot: ${'$'}{{ steps.${checkIfSnapshot.id}.outputs.is-snapshot }}'
                echo 'is-snapshot: ${'$'}{{ steps.check-if-snapshot-2.outputs.is-snapshot-2 }}'
                """.trimIndent(),
        )
        run(
            command = """
                cat ${'$'}GITHUB_OUTPUT
                echo ${'$'}GITHUB_OUTPUT
                """.trimIndent(),
        )
        run(
            command = "echo 'It's a snapshot!'",
            condition = expr("steps.${checkIfSnapshot.id}.outputs.is-snapshot == 'true'"),
        )
        run(
            command = "echo 'It's NOT a snapshot!'",
            condition = expr("steps.${checkIfSnapshot.id}.outputs.is-snapshot == 'false'"),
        )
    }
}.writeToFile()
