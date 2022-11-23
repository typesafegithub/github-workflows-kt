@file:Suppress("TooManyFunctions", "SpreadOperator")

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

internal fun List<Trigger>.triggersToYaml(): Map<String, Any?> =
    this.associateBy(
        keySelector = { it.triggerName() },
        valueTransform = {
            val coreArguments = it.toMap()
            (coreArguments + it._customArguments)
                .ifEmpty { it.toAdditionalYaml() ?: emptyMap<Any, Any>() }
        },
    )

@InternalGithubActionsApi
public fun Trigger.toMap(): Map<String, List<String>> {
    return when (this) {
        is Push -> toMap()
        is PullRequest -> toMap()
        is PullRequestTarget -> toMap()
        else -> emptyMap()
    }
}

private fun Push.toMap() =
    mapOfNotNullValues(
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
        "tags" to tags,
        "tags-ignore" to tagsIgnore,
    )

private fun PullRequest.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.toSnakeCase() },
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
    )

private fun PullRequestTarget.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.toSnakeCase() },
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
    )

@Suppress("ComplexMethod")
@InternalGithubActionsApi
public fun Trigger.triggerName(): String = when (this) {
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

private fun Trigger.toAdditionalYaml(): Any? = when (this) {
    is Schedule -> toAdditionalYaml()
    is WorkflowDispatch -> toAdditionalYaml()
    else -> null
}

private fun Schedule.toAdditionalYaml(): List<Map<String, String>> =
    triggers.map { mapOf("cron" to it.expression) }

private fun WorkflowDispatch.toAdditionalYaml(): Map<String, Any?> = when {
    inputs.isEmpty() -> emptyMap()
    else -> mapOf(
        "inputs" to inputs.mapValues { (_, value) -> value.toYaml() },
    )
}

private fun WorkflowDispatch.Input.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "description" to description,
        "type" to type.toSnakeCase(),
        "required" to required,
        "default" to default,
        "options" to options.ifEmpty { null },
    )
