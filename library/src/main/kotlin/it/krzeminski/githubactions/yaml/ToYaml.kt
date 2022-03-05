package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.toBuilder
import kotlin.io.path.invariantSeparatorsPathString

private fun Workflow.generateYaml(addConsistencyCheck: Boolean, useGitDiff: Boolean): String {
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            name = "check_yaml_consistency",
            runsOn = UbuntuLatest,
        ) {
            uses("Check out", CheckoutV2())
            run("Install Kotlin", "sudo snap install --classic kotlin")
            if (useGitDiff) {
                run(
                    "Execute script",
                    "rm '$targetFile' && " + sourceFile.invariantSeparatorsPathString
                )
                run(
                    "Consistency check",
                    "git diff --exit-code '${targetFile.invariantSeparatorsPathString}'"
                )
            } else {
                run(
                    "Consistency check",
                    "diff -u '${targetFile.invariantSeparatorsPathString}' " +
                        "<('${sourceFile.invariantSeparatorsPathString}')"
                )
            }
        }
        listOf(consistencyCheckJob) + jobs.map {
            it.copy(needs = it.needs + consistencyCheckJob)
        }
    } else {
        jobs
    }

    return buildString {
        appendLine(
            """
            # This file was generated using Kotlin DSL (${sourceFile.invariantSeparatorsPathString}).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl
            """.trimIndent()
        )
        appendLine()
        appendLine("name: $name")
        appendLine()
        appendLine("on:")
        appendLine(this@toYaml.on.triggersToYaml().prependIndent("  "))
        appendLine()

        if (this@toYaml.env.isNotEmpty()) {
            appendLine("env:")
            appendLine(this@toYaml.env.toYaml().prependIndent("  "))
            appendLine()
        }

        appendLine("jobs:")
        append(jobsWithConsistencyCheck.jobsToYaml().prependIndent("  "))
    }
}

fun Workflow.toYaml(addConsistencyCheck: Boolean = true): String {
    return generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = false,
    )
}

fun Workflow.writeToFile(addConsistencyCheck: Boolean = true) {
    val yaml = generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = true,
    )
    this.targetFile.toFile().writeText(yaml)
}
