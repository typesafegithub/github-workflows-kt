@file:Suppress("TooManyFunctions")

package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.triggers.BranchProtectionRule
import it.krzeminski.githubactions.domain.triggers.CheckRun
import it.krzeminski.githubactions.domain.triggers.CheckSuite
import it.krzeminski.githubactions.domain.triggers.Create
import it.krzeminski.githubactions.domain.triggers.Delete
import it.krzeminski.githubactions.domain.triggers.Deployment
import it.krzeminski.githubactions.domain.triggers.DeploymentStatus
import it.krzeminski.githubactions.domain.triggers.Discussion
import it.krzeminski.githubactions.domain.triggers.DiscussionComment
import it.krzeminski.githubactions.domain.triggers.Fork
import it.krzeminski.githubactions.domain.triggers.Gollum
import it.krzeminski.githubactions.domain.triggers.IssueComment
import it.krzeminski.githubactions.domain.triggers.Issues
import it.krzeminski.githubactions.domain.triggers.Label
import it.krzeminski.githubactions.domain.triggers.Milestone
import it.krzeminski.githubactions.domain.triggers.PageBuild
import it.krzeminski.githubactions.domain.triggers.Project
import it.krzeminski.githubactions.domain.triggers.ProjectCard
import it.krzeminski.githubactions.domain.triggers.ProjectColumn
import it.krzeminski.githubactions.domain.triggers.PublicWorkflow
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestReview
import it.krzeminski.githubactions.domain.triggers.PullRequestReviewComment
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.RegistryPackage
import it.krzeminski.githubactions.domain.triggers.Release
import it.krzeminski.githubactions.domain.triggers.RepositoryDispatch
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Status
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.Watch
import it.krzeminski.githubactions.domain.triggers.WorkflowCall
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowRun
import it.krzeminski.githubactions.internal.InternalGithubActionsApi

fun List<Trigger>.triggersToYaml(): String =
    joinToString(separator = "\n") { it.toYaml() }

fun Trigger.toYaml(): String =
    (
        toYamlFromMap() +
            toAdditionalYaml() +
            customArgumentsToYaml().let { if (it.isNotEmpty()) it.prependIndent("  ") else it }
        ).removeSuffix("\n")

private typealias MapOfYaml = LinkedHashMap<String, List<String>?>

private fun Trigger.toYamlFromMap() = buildString {
    val trigger = this@toYamlFromMap
    appendLine("${trigger.triggerName()}:")
    for ((property, items) in trigger.toMap()) {
        printIfHasElements(items, property)
    }
}

@InternalGithubActionsApi
fun Trigger.toMap(): MapOfYaml {
    return when (this) {
        is Push -> toMap()
        is PullRequest -> toMap()
        is PullRequestTarget -> toMap()
        else -> LinkedHashMap()
    }
}

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

@Suppress("ComplexMethod")
@InternalGithubActionsApi
fun Trigger.triggerName() = when (this) {
    is PullRequest -> "pull_request"
    is PullRequestTarget -> "pull_request_target"
    is Push -> "push"
    is Schedule -> "schedule"
    is WorkflowDispatch -> "workflow_dispatch"
    is BranchProtectionRule -> "branch_protection_rule"
    is CheckRun -> "check_run"
    is CheckSuite -> "check_suite"
    is Create -> "create"
    is Delete -> "delete"
    is Deployment -> "deployment"
    is DeploymentStatus -> "deployment_status"
    is Discussion -> "discussion"
    is DiscussionComment -> "discussion_comment"
    is Fork -> "fork"
    is Gollum -> "gollum"
    is IssueComment -> "issue_comment"
    is Issues -> "issues"
    is Label -> "label"
    is Milestone -> "milestone"
    is PageBuild -> "page_build"
    is Project -> "project"
    is ProjectCard -> "project_card"
    is ProjectColumn -> "project_column"
    is PublicWorkflow -> "public"
    is PullRequestReview -> "pull_request_review"
    is PullRequestReviewComment -> "pull_request_review_comment"
    is RegistryPackage -> "registry_package"
    is Release -> "release"
    is RepositoryDispatch -> "repository_dispatch"
    is Status -> "status"
    is Watch -> "watch"
    is WorkflowCall -> "workflow_call"
    is WorkflowRun -> "workflow_run"
}

private fun Trigger.toAdditionalYaml(): String = when (this) {
    is Schedule -> toAdditionalYaml()
    is WorkflowDispatch -> toAdditionalYaml()
    else -> ""
}

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
    appendLine("${space}type: ${type.toSnakeCase()}")
    appendLine("${space}required: $required")
    if (default != null) appendLine("${space}default: '$default'")
    printIfHasElements(options, "options", space = "      ")
}.removeSuffix("\n")
