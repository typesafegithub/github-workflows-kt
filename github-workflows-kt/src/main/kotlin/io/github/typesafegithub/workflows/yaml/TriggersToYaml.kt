@file:Suppress("TooManyFunctions", "SpreadOperator")

package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.triggers.BranchProtectionRule
import io.github.typesafegithub.workflows.domain.triggers.CheckRun
import io.github.typesafegithub.workflows.domain.triggers.CheckSuite
import io.github.typesafegithub.workflows.domain.triggers.Create
import io.github.typesafegithub.workflows.domain.triggers.Delete
import io.github.typesafegithub.workflows.domain.triggers.Deployment
import io.github.typesafegithub.workflows.domain.triggers.DeploymentStatus
import io.github.typesafegithub.workflows.domain.triggers.Discussion
import io.github.typesafegithub.workflows.domain.triggers.DiscussionComment
import io.github.typesafegithub.workflows.domain.triggers.Fork
import io.github.typesafegithub.workflows.domain.triggers.Gollum
import io.github.typesafegithub.workflows.domain.triggers.ImageVersion
import io.github.typesafegithub.workflows.domain.triggers.IssueComment
import io.github.typesafegithub.workflows.domain.triggers.Issues
import io.github.typesafegithub.workflows.domain.triggers.Label
import io.github.typesafegithub.workflows.domain.triggers.MergeGroup
import io.github.typesafegithub.workflows.domain.triggers.Milestone
import io.github.typesafegithub.workflows.domain.triggers.PageBuild
import io.github.typesafegithub.workflows.domain.triggers.Project
import io.github.typesafegithub.workflows.domain.triggers.ProjectCard
import io.github.typesafegithub.workflows.domain.triggers.ProjectColumn
import io.github.typesafegithub.workflows.domain.triggers.PublicWorkflow
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequestReview
import io.github.typesafegithub.workflows.domain.triggers.PullRequestReviewComment
import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.RegistryPackage
import io.github.typesafegithub.workflows.domain.triggers.Release
import io.github.typesafegithub.workflows.domain.triggers.RepositoryDispatch
import io.github.typesafegithub.workflows.domain.triggers.Schedule
import io.github.typesafegithub.workflows.domain.triggers.Status
import io.github.typesafegithub.workflows.domain.triggers.Trigger
import io.github.typesafegithub.workflows.domain.triggers.Watch
import io.github.typesafegithub.workflows.domain.triggers.WorkflowCall
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.domain.triggers.WorkflowRun
import io.github.typesafegithub.workflows.internal.InternalGithubActionsApi

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
public fun Trigger.toMap(): Map<String, List<String>> =
    when (this) {
        is BranchProtectionRule -> toMap()
        is CheckRun -> toMap()
        is CheckSuite -> toMap()
        is Discussion -> toMap()
        is DiscussionComment -> toMap()
        is IssueComment -> toMap()
        is Issues -> toMap()
        is Label -> toMap()
        is MergeGroup -> toMap()
        is Milestone -> toMap()
        is Project -> toMap()
        is ProjectCard -> toMap()
        is ProjectColumn -> toMap()
        is PullRequest -> toMap()
        is PullRequestReview -> toMap()
        is PullRequestReviewComment -> toMap()
        is PullRequestTarget -> toMap()
        is Push -> toMap()
        is RegistryPackage -> toMap()
        is Release -> toMap()
        is RepositoryDispatch -> toMap()
        is Watch -> toMap()
        is WorkflowRun -> toMap()
        else -> emptyMap()
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
        "types" to types.ifEmpty { null }?.map { it.name },
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
    )

private fun PullRequestTarget.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
    )

private fun RepositoryDispatch.toMap() =
    mapOfNotNullValues(
        "types" to types,
    )

private fun BranchProtectionRule.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun CheckRun.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun CheckSuite.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Discussion.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun DiscussionComment.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun IssueComment.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Issues.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Label.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun MergeGroup.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Milestone.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Project.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun ProjectCard.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun ProjectColumn.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun PullRequestReview.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun PullRequestReviewComment.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun RegistryPackage.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun Release.toMap() =
    mapOfNotNullValues(
        "types" to types,
    )

private fun Watch.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

private fun WorkflowRun.toMap() =
    mapOfNotNullValues(
        "types" to types.ifEmpty { null }?.map { it.name },
    )

@Suppress("ComplexMethod")
@InternalGithubActionsApi
public fun Trigger.triggerName(): String =
    when (this) {
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
        is ImageVersion -> "image_version"
        is IssueComment -> "issue_comment"
        is Issues -> "issues"
        is Label -> "label"
        is MergeGroup -> "merge_group"
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

private fun Trigger.toAdditionalYaml(): Any? =
    when (this) {
        is Schedule -> toAdditionalYaml()
        is WorkflowDispatch -> toAdditionalYaml()
        is WorkflowCall -> toAdditionalYaml()
        else -> null
    }

private fun Schedule.toAdditionalYaml(): List<Map<String, String>> = triggers.map { mapOf("cron" to it.expression) }

private fun WorkflowDispatch.toAdditionalYaml(): Map<String, Any?> =
    when {
        inputs.isEmpty() -> {
            emptyMap()
        }

        else -> {
            mapOf(
                "inputs" to inputs.mapValues { (_, value) -> value.toYaml() },
            )
        }
    }

private fun WorkflowDispatch.Input.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "description" to description,
        "type" to type.name,
        "required" to required,
        "default" to default,
        "options" to options.ifEmpty { null },
    )

private fun WorkflowCall.toAdditionalYaml(): Map<String, Any?> =
    when {
        inputs.isEmpty() -> {
            emptyMap()
        }

        else -> {
            mapOfNotNullValues(
                "inputs" to inputs.mapValues { (_, value) -> value.toYaml() },
                "outputs" to outputs?.mapValues { (_, value) -> value.toYaml() },
                "secrets" to secrets?.mapValues { (_, value) -> value.toYaml() },
            )
        }
    }

private fun WorkflowCall.Input.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "description" to description,
        "type" to type.name,
        "required" to required,
        "default" to default,
    )

private fun WorkflowCall.Output.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "description" to description,
        "value" to value,
    )

private fun WorkflowCall.Secret.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "description" to description,
        "required" to required,
    )
