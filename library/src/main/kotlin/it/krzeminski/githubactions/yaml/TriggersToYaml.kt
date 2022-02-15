package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch

fun List<Trigger>.triggersToYaml(): String =
    this
        .map { it.toYamlString() }
        .joinToString(separator = "\n") { it }

private fun Trigger.toYamlString() =
    when (this) {
        is WorkflowDispatch -> "workflow_dispatch:"
        is Push -> toYaml()
        is PullRequest -> toYaml()
        is PullRequestTarget -> toYaml()
        is Schedule -> toYaml()
    }

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
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

private fun PullRequestTarget.toYaml() = buildString {
    appendLine("pull_request_target:")
    printIfHasElements(this@toYaml.types.map(PullRequestTarget.Type::name), "types")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

private fun Schedule.toYaml() = buildString {
    appendLine("schedule:")
    this@toYaml.triggers.forEach {
        appendLine(" - cron: '${it.expression}'")
    }
}.removeSuffix("\n")

private fun StringBuilder.printIfHasElements(items: List<String>?, name: String) {
    if (!items.isNullOrEmpty()) {
        appendLine("  $name:")
        items.forEach {
            appendLine("    - '$it'")
        }
    }
}
