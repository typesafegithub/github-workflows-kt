package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.ObjectCustomValue
import it.krzeminski.githubactions.dsl.StringCustomValue
import it.krzeminski.githubactions.dsl.toBuilder
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.relativeTo

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
    rootDir.resolve(targetFile).toFile().writeText(yaml)
}

private fun Path.getPathString(base: Path): String =
    absolute().relativeTo(base.absolute()).invariantSeparatorsPathString

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
                    "rm '${targetFile.getPathString(rootDir)}' " +
                        "&& '${sourceFile.getPathString(rootDir)}'"
                )
                run(
                    "Consistency check",
                    "git diff --exit-code '${targetFile.getPathString(rootDir)}'"
                )
            } else {
                run(
                    "Consistency check",
                    "diff -u '${targetFile.getPathString(rootDir)}' " +
                        "<('${sourceFile.getPathString(rootDir)}')"
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
            # This file was generated using Kotlin DSL (${sourceFile.getPathString(rootDir)}).
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

        if (concurrency != null) {
            appendLine("concurrency:")
            appendLine("  group: ${concurrency.group}")
            appendLine("  cancel-in-progress: ${concurrency.cancelInProgress}")
            appendLine()
        }

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

        appendLine()
    }.also { yamlContent ->
        failIfMalformedYml(yamlContent)
    }
}

internal fun HasCustomArguments.customArgumentsToYaml(): String = buildString {
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
