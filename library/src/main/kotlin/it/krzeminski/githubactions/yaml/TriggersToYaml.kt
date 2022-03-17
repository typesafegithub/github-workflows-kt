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
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type
import it.krzeminski.githubactions.domain.triggers.WorkflowRun
import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.dsl.ListFreeArg
import it.krzeminski.githubactions.dsl.StringFreeArg
import kotlin.reflect.KClass

fun List<Trigger>.triggersToYaml(): String =
    joinToString(separator = "\n") { it.toYaml() }

fun Trigger.toYaml(): String =
    (toYamlFromMap() + toAdditionalYaml() + freeArgsToYaml()).removeSuffix("\n")

typealias MapOfYaml = LinkedHashMap<String, List<String>?>

@Suppress("ComplexMethod")
fun Trigger.toMap(): MapOfYaml {
    return when (this) {
        is Push -> toMap()
        is PullRequest -> toMap()
        is PullRequestTarget -> toMap()
        is WorkflowDispatch -> emptyMap
        is Schedule -> emptyMap
        is BranchProtectionRule -> emptyMap
        is CheckRun -> emptyMap
        is CheckSuite -> emptyMap
        is Create -> emptyMap
        is Delete -> emptyMap
        is Deployment -> emptyMap
        is DeploymentStatus -> emptyMap
        is Discussion -> emptyMap
        is DiscussionComment -> emptyMap
        is Fork -> emptyMap
        is Gollum -> emptyMap
        is IssueComment -> emptyMap
        is Issues -> emptyMap
        is Label -> emptyMap
        is Milestone -> emptyMap
        is PageBuild -> emptyMap
        is Project -> emptyMap
        is ProjectCard -> emptyMap
        is ProjectColumn -> emptyMap
        is PublicWorkflow -> emptyMap
        is PullRequestReview -> emptyMap
        is PullRequestReviewComment -> emptyMap
        is RegistryPackage -> emptyMap
        is Release -> emptyMap
        is RepositoryDispatch -> emptyMap
        is Status -> emptyMap
        is Watch -> emptyMap
        is WorkflowCall -> emptyMap
        is WorkflowRun -> emptyMap
    }
}

private val emptyMap = LinkedHashMap<String, List<String>?>()

val Trigger.triggerName: String get() = triggerClassMap
    .firstOrNull { it.second == this::class }
    ?.first
    ?: error("Couldn't find triggerName for class ${this::class}")

val triggerClassMap: List<Pair<String, KClass<*>>> = listOf(
    "pull_request" to PullRequest::class,
    "pull_request_target" to PullRequestTarget::class,
    "push" to Push::class,
    "schedule" to Schedule::class,
    "workflow_dispatch" to WorkflowDispatch::class,
    "branch_protection_rule" to BranchProtectionRule::class,
    "check_run" to CheckRun::class,
    "check_suite" to CheckSuite::class,
    "create" to Create::class,
    "delete" to Delete::class,
    "deployment" to Deployment::class,
    "deployment_status" to DeploymentStatus::class,
    "discussion" to Discussion::class,
    "discussion_comment" to DiscussionComment::class,
    "fork" to Fork::class,
    "gollum" to Gollum::class,
    "issue_comment" to IssueComment::class,
    "issues" to Issues::class,
    "label" to Label::class,
    "milestone" to Milestone::class,
    "page_build" to PageBuild::class,
    "project" to Project::class,
    "project_card" to ProjectCard::class,
    "project_column" to ProjectColumn::class,
    "public" to PublicWorkflow::class,
    "pull_request_review" to PullRequestReview::class,
    "pull_request_review_comment" to PullRequestReviewComment::class,
    "registry_package" to RegistryPackage::class,
    "release" to Release::class,
    "repository_dispatch" to RepositoryDispatch::class,
    "status" to Status::class,
    "watch" to Watch::class,
    "workflow_call" to WorkflowCall::class,
    "workflow_run" to WorkflowRun::class,
)

internal fun HasCustomArguments.freeArgsToYaml(): String = buildString {
    for (arg in _customArguments) {
        when (arg) {
            is ListFreeArg -> printIfHasElements(arg.value, arg.key)
            is StringFreeArg -> appendLine("  ${arg.key}: ${arg.value}")
        }
    }
}.removeSuffix("\n")

private fun Trigger.toYamlFromMap() = buildString {
    val trigger = this@toYamlFromMap
    appendLine("${trigger.triggerName}:")
    for ((property, items) in trigger.toMap()) {
        printIfHasElements(items, property)
    }
}

@Suppress("ComplexMethod")
private fun Trigger.toAdditionalYaml(): String = when (this) {
    is Schedule -> toAdditionalYaml()
    is WorkflowDispatch -> toAdditionalYaml()
    is Push -> ""
    is PullRequest -> ""
    is PullRequestTarget -> ""
    is BranchProtectionRule -> ""
    is CheckRun -> ""
    is CheckSuite -> ""
    is Create -> ""
    is Delete -> ""
    is Deployment -> ""
    is DeploymentStatus -> ""
    is Discussion -> ""
    is DiscussionComment -> ""
    is Fork -> ""
    is Gollum -> ""
    is IssueComment -> ""
    is Issues -> ""
    is Label -> ""
    is Milestone -> ""
    is PageBuild -> ""
    is Project -> ""
    is ProjectCard -> ""
    is ProjectColumn -> ""
    is PublicWorkflow -> ""
    is PullRequestReview -> ""
    is PullRequestReviewComment -> ""
    is RegistryPackage -> ""
    is Release -> ""
    is RepositoryDispatch -> ""
    is Status -> ""
    is Watch -> ""
    is WorkflowCall -> ""
    is WorkflowRun -> ""
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
