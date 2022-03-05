@file:Suppress("TooManyFunctions")

package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs
import it.krzeminski.githubactions.dsl.ListFreeArg
import it.krzeminski.githubactions.dsl.StringFreeArg

fun List<Trigger>.triggersToYaml(): String =
    this
        .map { it.toYamlString() }
        .joinToString(separator = "\n") { it }

private fun Trigger.toYamlString() =
    when (this) {
        is WorkflowDispatch -> toYaml()
        is Push -> toYaml()
        is PullRequest -> toYaml()
        is PullRequestTarget -> toYaml()
        is Schedule -> toYaml()
    } + freeArgsToYaml()

internal fun HasFreeYamlArgs.freeArgsToYaml(): String = buildString {
    append("\n")
    for (arg in freeYamlArgs) {
        when (arg) {
            is ListFreeArg -> printIfHasElements(arg.value, arg.key)
            is StringFreeArg -> appendLine("  ${arg.key}: ${arg.value}")
        }
    }
}.removeSuffix("\n")

private fun Push.toYaml() = buildString {
    appendLine("push:")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.tags, "tags")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.tagsIgnore, "tags-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

private fun PullRequest.toYaml() = buildString {
    appendLine("pull_request:")
    printIfHasElements(this@toYaml.types.map(PullRequest.Type::toYaml), "types")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

private fun PullRequestTarget.toYaml() = buildString {
    appendLine("pull_request_target:")
    printIfHasElements(this@toYaml.types.map(PullRequestTarget.Type::toYaml), "types")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

private fun PullRequestTarget.Type.toYaml(): String = this.toSnakeCase()

private fun PullRequest.Type.toYaml(): String = this.toSnakeCase()

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

private fun Type.toYaml(): String = when (this) {
    Type.Choice -> "choice"
    Type.Environment -> "environment"
    Type.Boolean -> "boolean"
    Type.String -> "string"
}

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
