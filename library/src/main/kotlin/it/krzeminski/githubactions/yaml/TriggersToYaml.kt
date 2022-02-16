package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type as WDT

fun List<Trigger>.triggersToYaml(): String =
    this
        .map { it.toYamlString() }
        .joinToString(separator = "\n") { it }

private fun Trigger.toYamlString() =
    when (this) {
        is WorkflowDispatch -> toYaml()
        is Push -> toYaml()
        is PullRequest -> toYaml()
        is Schedule -> toYaml()
    }

private fun Push.toYaml() = buildString {
    appendLine("push:")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.tags, "tags")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.tagsIgnore, "tags-ignore")
}.removeSuffix("\n")

private fun PullRequest.toYaml() = buildString {
    appendLine("pull_request:")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
}.removeSuffix("\n")

private fun Schedule.toYaml() = buildString {
    appendLine("schedule:")
    this@toYaml.triggers.forEach {
        appendLine(" - cron: '${it.expression}'")
    }
}.removeSuffix("\n")

private fun WorkflowDispatch.toYaml(): String = buildString {
    appendLine("workflow_dispatch:")
    if (inputs.isNotEmpty()) {
        appendLine("  inputs:")
        for ((key, input) in inputs) {
            appendLine("    $key:")
            appendLine(input.toYaml())
        }
    }
}.removeSuffix("\n")

private fun WorkflowDispatch.Input.toYaml(): String = buildString {
    val space = "      "
    appendLine("${space}description: '$description'")
    appendLine("${space}type: ${type.toYaml()}")
    appendLine("${space}required: $required")
    if (default != null) appendLine("${space}default: '$default'")
    printIfHasElements(options, "options", space = "      ")
}.removeSuffix("\n")

// WDT = WorkflowDispatch.Type
private fun WDT.toYaml(): String = when (this) {
    WDT.Choice -> "choice"
    WDT.Environment -> "environment"
    WDT.Boolean -> "boolean"
    WDT.String -> "string"
}

private fun StringBuilder.printIfHasElements(
    items: List<String>?,
    name: String,
    space: String = "  ",
) {
    if (!items.isNullOrEmpty()) {
        appendLine("$space$name:")
        items.forEach {
            appendLine("$space  - '$it'")
        }
    }
}
