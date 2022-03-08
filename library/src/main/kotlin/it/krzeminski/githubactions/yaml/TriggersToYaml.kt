@file:Suppress("TooManyFunctions")

package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type

typealias MapOfYaml = java.util.LinkedHashMap<String, List<String>?>

fun List<Trigger>.triggersToYaml(): String =
    map { it.toYaml() }.joinToString(separator = "\n")

fun Trigger.toYaml() =
    (toYamlFromMap() + toAdditionalYaml()).removeSuffix("\n")

fun Trigger.toMap(): MapOfYaml =
    when (this) {
        is WorkflowDispatch -> LinkedHashMap()
        is Push -> toMap()
        is PullRequest -> toMap()
        is PullRequestTarget -> toMap()
        is Schedule -> LinkedHashMap()
    }

private fun Trigger.toYamlFromMap() = buildString {
    val trigger = this@toYamlFromMap
    appendLine("${trigger.triggerName}:")
    for ((property, items) in trigger.toMap()) {
        printIfHasElements(items, property)
    }
}

private fun Trigger.toAdditionalYaml(): String = when (this) {
    is Push -> ""
    is PullRequest -> ""
    is PullRequestTarget -> ""
    is Schedule -> toAdditionalYaml()
    is WorkflowDispatch -> toAdditionalYaml()
}.removeSuffix("\n")

private fun Push.toMap(): MapOfYaml = linkedMapOf(
    "branches" to branches,
    "tags" to tags,
    "branches-ignore" to branchesIgnore,
    "tags-ignore" to tagsIgnore,
    "paths" to paths,
    "paths-ignore" to pathsIgnore,
)

private fun PullRequest.toMap(): MapOfYaml = linkedMapOf(
    "types" to types.toSnakeCase(),
    "branches" to branches,
    "branches-ignore" to branchesIgnore,
    "paths" to paths,
    "paths-ignore" to pathsIgnore,
)

private fun PullRequestTarget.toMap(): MapOfYaml = linkedMapOf(
    "types" to types.toSnakeCase(),
    "branches" to branches,
    "branches-ignore" to branchesIgnore,
    "paths" to paths,
    "paths-ignore" to pathsIgnore,
)

private fun Schedule.toAdditionalYaml() =
    triggers.joinToString("\n") { cron ->
        " - cron: '${cron.expression}'"
    }

private fun WorkflowDispatch.toAdditionalYaml(): String = when {
    inputs.isEmpty() -> ""
    else -> {
        val inputsToYaml = inputs
            .entries
            .joinToString("\n") { (key, input) ->
                "    $key:\n${input.toYaml()}"
            }
        "  inputs:\n$inputsToYaml"
    }
}

private fun WorkflowDispatch.Input.toYaml(): String = buildString {
    val space = "      "
    appendLine("${space}description: '$description'")
    appendLine("${space}type: ${type.toYaml()}")
    appendLine("${space}required: $required")
    if (default != null) appendLine("${space}default: '$default'")
    printIfHasElements(options, "options", space = "      ")
}.removeSuffix("\n")

private fun Type.toYaml(): String = when (this) {
    Type.Choice -> "choice"
    Type.Environment -> "environment"
    Type.Boolean -> "boolean"
    Type.String -> "string"
}

private fun StringBuilder.printIfHasElements(
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
