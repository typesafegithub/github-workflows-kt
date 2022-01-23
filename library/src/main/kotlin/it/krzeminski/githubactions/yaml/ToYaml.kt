package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.toBuilder

fun Workflow.toYaml(addConsistencyCheck: Boolean = true): String {
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            name = "check_yaml_consistency",
            runsOn = UbuntuLatest,
        ) {
            uses("Check out", CheckoutV2())
            run("Install Kotlin", "sudo snap install --classic kotlin")
            run("Consistency check", "diff -u '$targetFile' <('$sourceFile')")
        }
        listOf(consistencyCheckJob) + jobs.map {
            it.copy(needs = listOf(consistencyCheckJob))
        }
    } else {
        jobs
    }

    return """|# This file was generated using Kotlin DSL ($sourceFile).
              |# If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
              |
              |name: $name
              |
              |on:
              |${this.on.triggersToYaml().prependIndent("  ")}
              |
              |jobs:
              |${jobsWithConsistencyCheck.jobsToYaml().prependIndent("  ")}""".trimMargin()
}

fun Workflow.writeToFile() {
    val yaml = this.toYaml(
        // Because the current consistency check logic relies on writing to standard output.
        addConsistencyCheck = false,
    )
    this.targetFile.toFile().writeText(yaml)
}
