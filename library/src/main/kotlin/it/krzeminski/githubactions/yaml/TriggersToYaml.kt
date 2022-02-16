package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Assigned
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.AutoMergeDisabled
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.AutoMergeEnabled
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Closed
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.ConvertedToDraft
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Edited
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Labeled
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Locked
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Opened
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.ReadyForReview
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Reopened
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.ReviewRequestRemoved
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.ReviewRequested
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Synchronize
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Unassigned
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Unlabeled
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget.Type.Unlocked
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
    printIfHasElements(this@toYaml.types.map(PullRequestTarget.Type::toYaml), "types")
    printIfHasElements(this@toYaml.branches, "branches")
    printIfHasElements(this@toYaml.branchesIgnore, "branches-ignore")
    printIfHasElements(this@toYaml.paths, "paths")
    printIfHasElements(this@toYaml.pathsIgnore, "paths-ignore")
}.removeSuffix("\n")

@Suppress("ComplexMethod")
private fun PullRequestTarget.Type.toYaml(): String = when (this) {
    Assigned -> "assigned"
    Unassigned -> "unassigned"
    Labeled -> "labeled"
    Unlabeled -> "unlabeled"
    Opened -> "opened"
    Edited -> "edited"
    Closed -> "closed"
    Reopened -> "reopened"
    Synchronize -> "synchronize"
    ConvertedToDraft -> "converted_to_draft"
    ReadyForReview -> "ready_for_review"
    Locked -> "locked"
    Unlocked -> "unlocked"
    ReviewRequested -> "review_requested"
    ReviewRequestRemoved -> "review_request_removed"
    AutoMergeEnabled -> "auto_merge_enabled"
    AutoMergeDisabled -> "auto_merge_disabled"
}

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
