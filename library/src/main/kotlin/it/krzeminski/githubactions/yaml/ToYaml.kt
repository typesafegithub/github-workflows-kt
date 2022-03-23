package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.ObjectCustomValue
import it.krzeminski.githubactions.dsl.StringCustomValue
import it.krzeminski.githubactions.dsl.toBuilder
import kotlin.io.path.invariantSeparatorsPathString

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

@Suppress("LongMethod")
private fun Workflow.generateYaml(addConsistencyCheck: Boolean, useGitDiff: Boolean): String {
    val workflow = this
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            id = "check_yaml_consistency",
            runsOn = UbuntuLatest,
        ) {
            uses("Check out", CheckoutV3())
            if (useGitDiff) {
                run(
                    "Execute script",
                    "rm '${targetFile.invariantSeparatorsPathString}' " +
                        "&& '${sourceFile.invariantSeparatorsPathString}'"
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
        appendLine(workflow.on.triggersToYaml().prependIndent("  "))
        appendLine()

        if (workflow.env.isNotEmpty()) {
            appendLine("env:")
            appendLine(workflow.env.toYaml().prependIndent("  "))
            appendLine()
        }

        appendLine("jobs:")
        append(jobsWithConsistencyCheck.jobsToYaml().prependIndent("  "))
        customArgumentsToYaml().takeIf { it.isNotBlank() }
            ?.let { freeargs ->
                append("\n")
                append(freeargs.replaceIndent(""))
            }
    }
}

internal fun HasCustomArguments.customArgumentsToYaml(): String = buildString {
    append("\n")
    for ((key, customValue) in _customArguments) {
        when (customValue) {
            is ListCustomValue -> printIfHasElements(customValue.value, key)
            is StringCustomValue -> appendLine("  $key: ${customValue.value}")
            is ObjectCustomValue -> {
                appendLine("  $key:")
                for ((subkey, subvalue) in customValue.value) {
                    appendLine("    $subkey: $subvalue")
                }
            }
        }
    }
}.removeSuffix("\n")

internal fun StringBuilder.printIfHasElements(
    items: List<String>?,
    name: String,
    space: String = "  ",
) {
    if (!items.isNullOrEmpty()) {
        appendLine("$name:".prependIndent(space))
        items.forEach {
            appendLine("  - '$it'".prependIndent(space))
        }
    }
}
