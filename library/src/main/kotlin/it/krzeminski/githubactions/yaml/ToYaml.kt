package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.toBuilder
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.pathString

fun Workflow.toYaml(addConsistencyCheck: Boolean = true): String {
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            name = "check_yaml_consistency",
            runsOn = UbuntuLatest,
        ) {
            uses("Check out", CheckoutV2())
            run("Install Kotlin", "sudo snap install --classic kotlin")
            run("Execute script", sourceFile.invariantSeparatorsPathString)
            run(
                "Consistency check",
                "test \$(git diff '${targetFile.invariantSeparatorsPathString}' | wc -l) -eq 0"
            )
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

fun Workflow.writeToFile(addConsistencyCheck: Boolean = true) {
    val yaml = this.toYaml(
        addConsistencyCheck = addConsistencyCheck,
    )
    this.targetFile.toFile().writeText(yaml)
}
