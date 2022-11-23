package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.toBuilder
import it.krzeminski.githubactions.internal.findGitRoot
import it.krzeminski.githubactions.internal.relativeToAbsolute
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.invariantSeparatorsPathString

public fun Workflow.toYaml(
    addConsistencyCheck: Boolean = true,
    gitRootDir: Path = sourceFile.absolute().findGitRoot(),
): String {
    return generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = false,
        gitRootDir = gitRootDir,
    )
}

public fun Workflow.writeToFile(
    addConsistencyCheck: Boolean = true,
    gitRootDir: Path = sourceFile.absolute().findGitRoot(),
) {
    val yaml = generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = true,
        gitRootDir = gitRootDir,
    )
    gitRootDir.resolve(".github").resolve("workflows").resolve(this.targetFileName).toFile().let {
        it.parentFile.mkdirs()
        it.writeText(yaml)
    }
}

@Suppress("LongMethod")
private fun Workflow.generateYaml(addConsistencyCheck: Boolean, useGitDiff: Boolean, gitRootDir: Path): String {
    val sourceFilePath = sourceFile.relativeToAbsolute(gitRootDir).invariantSeparatorsPathString
    val targetFilePath = gitRootDir.resolve(".github").resolve("workflows").resolve(this.targetFileName)
        .relativeToAbsolute(gitRootDir).invariantSeparatorsPathString
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            id = "check_yaml_consistency",
            runsOn = UbuntuLatest,
            condition = yamlConsistencyJobCondition,
        ) {
            uses("Check out", CheckoutV3())
            if (useGitDiff) {
                run(
                    "Execute script",
                    "rm '$targetFilePath' " +
                        "&& '$sourceFilePath'",
                )
                run(
                    "Consistency check",
                    "git diff --exit-code '$targetFilePath'",
                )
            } else {
                run(
                    "Consistency check",
                    "diff -u '$targetFilePath' " +
                        "<('$sourceFilePath')",
                )
            }
        }
        listOf(consistencyCheckJob) + jobs.map {
            it.copy(needs = it.needs + consistencyCheckJob)
        }
    } else {
        jobs
    }

    val preamble = """
        # This file was generated using Kotlin DSL ($sourceFilePath).
        # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
        # Generated with https://github.com/krzema12/github-workflows-kt
    """.trimIndent()

    val workflowToBeSerialized = this.toYamlInternal(jobsWithConsistencyCheck)
    val workflowAsYaml = workflowToBeSerialized.toYaml()

    return preamble + "\n\n" + workflowAsYaml
}

@Suppress("SpreadOperator")
private fun Workflow.toYamlInternal(jobsWithConsistencyCheck: List<Job<*>>): Map<String, Any> =
    mapOfNotNullValues(
        "name" to name,
        "on" to on.triggersToYaml(),
        "concurrency" to concurrency?.let {
            mapOf(
                "group" to it.group,
                "cancel-in-progress" to it.cancelInProgress,
            )
        },
        "env" to env.ifEmpty { null },
        "jobs" to jobsWithConsistencyCheck.jobsToYaml(),
        *_customArguments.toList().toTypedArray(),
    )
