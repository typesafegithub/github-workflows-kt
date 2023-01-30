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
    gitRootDir: Path? = sourceFile?.absolute()?.findGitRoot(),
): String {
    return generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = false,
        gitRootDir = gitRootDir,
    )
}

public fun Workflow.toYaml(): String = toYaml(
    addConsistencyCheck = sourceFile != null,
)

public fun Workflow.writeToFile(
    addConsistencyCheck: Boolean = true,
    gitRootDir: Path? = sourceFile?.absolute()?.findGitRoot(),
) {
    checkNotNull(gitRootDir) {
        "gitRootDir must be specified explicitly when sourceFile is null"
    }

    checkNotNull(this.targetFileName) {
        "targetFileName must not be null"
    }

    val yaml = generateYaml(
        addConsistencyCheck = addConsistencyCheck,
        useGitDiff = true,
        gitRootDir = gitRootDir,
    )

    gitRootDir.resolve(".github").resolve("workflows").resolve(targetFileName).toFile().let {
        it.parentFile.mkdirs()
        it.writeText(yaml)
    }
}

@Suppress("LongMethod")
private fun Workflow.generateYaml(
    addConsistencyCheck: Boolean,
    useGitDiff: Boolean,
    gitRootDir: Path?,
): String {
    val sourceFilePath = if (gitRootDir != null) {
        sourceFile?.relativeToAbsolute(gitRootDir)?.invariantSeparatorsPathString
    } else {
        null
    }

    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        check(gitRootDir != null && sourceFile != null) {
            "consistency check requires a valid sourceFile and Git root directory"
        }

        checkNotNull(targetFileName) {
            "consistency check requires a targetFileName"
        }

        val targetFilePath = gitRootDir.resolve(".github").resolve("workflows").resolve(targetFileName)
            .relativeToAbsolute(gitRootDir).invariantSeparatorsPathString

        val consistencyCheckJob = this.toBuilder().job(
            id = "check_yaml_consistency",
            name = "Check YAML consistency",
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

    /* preamble slightly differs on lines 1 and 2 when sourceFilePath is not available */
    val preamble = if (sourceFilePath != null) {
        """
        # This file was generated using Kotlin DSL ($sourceFilePath).
        # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
        # Generated with https://github.com/krzema12/github-workflows-kt
        """.trimIndent()
    } else {
        """
        # This file was generated using a Kotlin DSL.
        # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
        # Generated with https://github.com/krzema12/github-workflows-kt
        """.trimIndent()
    }

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
        *_customArguments.toList().toTypedArray(),
        "jobs" to jobsWithConsistencyCheck.jobsToYaml(),
    )
