package io.github.typesafegithub.workflows.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import io.github.typesafegithub.workflows.domain.triggers.BranchProtectionRule
import io.github.typesafegithub.workflows.domain.triggers.CheckRun
import io.github.typesafegithub.workflows.domain.triggers.CheckSuite
import io.github.typesafegithub.workflows.domain.triggers.Create
import io.github.typesafegithub.workflows.domain.triggers.Cron
import io.github.typesafegithub.workflows.domain.triggers.Delete
import io.github.typesafegithub.workflows.domain.triggers.Deployment
import io.github.typesafegithub.workflows.domain.triggers.DeploymentStatus
import io.github.typesafegithub.workflows.domain.triggers.Discussion
import io.github.typesafegithub.workflows.domain.triggers.DiscussionComment
import io.github.typesafegithub.workflows.domain.triggers.Fork
import io.github.typesafegithub.workflows.domain.triggers.Gollum
import io.github.typesafegithub.workflows.domain.triggers.IssueComment
import io.github.typesafegithub.workflows.domain.triggers.Issues
import io.github.typesafegithub.workflows.domain.triggers.Label
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
import io.github.typesafegithub.workflows.scriptmodel.ScheduleValue
import io.github.typesafegithub.workflows.scriptmodel.YamlTrigger
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflowTriggers
import io.github.typesafegithub.workflows.wrappergenerator.generation.toCamelCase
import io.github.typesafegithub.workflows.wrappergenerator.generation.toPascalCase
import io.github.typesafegithub.workflows.yaml.toMap
import io.github.typesafegithub.workflows.yaml.triggerName
import java.io.File

fun YamlWorkflowTriggers.toKotlin() = CodeBlock { builder ->
    builder.add("listOf(\n").indent()
        .add(pull_request.toKotlin())
        .add(push.toKotlin())
        .add(pull_request_target.toKotlin())
        .add(schedule.toKotlin())
        .add(workflow_dispatch.toKotlin())
        .add(branch_protection_rule.toKotlin("branch_protection_rule"))
        .add(check_run.toKotlin("check_run"))
        .add(check_suite.toKotlin("check_suite"))
        .add(create.toKotlin("create"))
        .add(delete.toKotlin("delete"))
        .add(deployment.toKotlin("deployment"))
        .add(deployment_status.toKotlin("deployment_status"))
        .add(discussion.toKotlin("discussion"))
        .add(discussion_comment.toKotlin("discussion_comment"))
        .add(fork.toKotlin("fork"))
        .add(gollum.toKotlin("gollum"))
        .add(issue_comment.toKotlin("issue_comment"))
        .add(issues.toKotlin("issues"))
        .add(label.toKotlin("label"))
        .add(merge_group.toKotlin("merge_group"))
        .add(milestone.toKotlin("milestone"))
        .add(page_build.toKotlin("page_build"))
        .add(public.toKotlin("public"))
        .add(project.toKotlin("project"))
        .add(project_card.toKotlin("project_card"))
        .add(project_column.toKotlin("project_column"))
        .add(pull_request_review.toKotlin("pull_request_review"))
        .add(pull_request_review_comment.toKotlin("pull_request_review_comment"))
        .add(registry_package.toKotlin("registry_package"))
        .add(repository_dispatch.toKotlin("repository_dispatch"))
        .add(release.toKotlin("release"))
        .add(status.toKotlin("status"))
        .add(watch.toKotlin("watch"))
        .add(workflow_call.toKotlin("workflow_call"))
        .add(workflow_run.toKotlin("workflow_run"))
        .add("),\n").unindent()
}

/**
 * BranchProtectionRule(_customArguments = mapOf(
"types" to ListCustomValue("created", "delated")
)).
 */
private fun YamlTrigger?.toKotlin(triggerName: String): CodeBlock {
    this ?: return CodeBlock.EMPTY

    val classname: ClassName = allTriggersMap[triggerName]?.classname()
        ?: error("Couldn't find class for triggerName=$triggerName")

    val typesCodeblock = if (types.isNullOrEmpty()) {
        CodeBlock.of("")
    } else {
        CodeBlock { builder ->
            builder
                .add("\n").indent()
                .add("_customArguments = %M(\n", Members.mapOf)
                .indent()
                .add("%S to %M", "types", Members.listOf)
                .add(types.joinToCode(separator = ", ", transform = { CodeBlock.of("%S", it) }))
                .unindent()
                .add("),\n")
                .unindent()
        }
    }

    return CodeBlock { builder ->
        builder.add("%T(", classname)
            .add(typesCodeblock)
            .add("),\n")
    }
}

fun Trigger?.toKotlin(): CodeBlock {
    if (this == null) {
        return CodeBlock.of("")
    }
    val map: Map<String, List<String>?> = this.toMap()
    return map.joinToCode(
        prefix = CodeBlock.of("%T(", classname()),
        postfix = "),",
        ifEmpty = CodeBlock.of("%T(),\n", classname()),
        newLineAtEnd = true,
    ) { key, value ->
        stringsOrEnums(key, value)?.let { list ->
            CodeBlock.of(
                "%N = %L",
                key.toCamelCase(),
                list.joinToString(separator = ", ", prefix = "listOf(", postfix = ")"),
            )
        }
    }
}

fun Trigger.stringsOrEnums(key: String, list: List<String>?) = when {
    list == null || list.isEmpty() -> null
    this is PullRequest && key == "types" -> list.map {
        val enum = enumValueOf<PullRequest.Type>(it.toPascalCase())
        "PullRequest.Type.$enum"
    }
    this is PullRequestTarget && key == "types" -> list.map {
        val enum = enumValueOf<PullRequestTarget.Type>(it.toPascalCase())
        "PullRequestTarget.Type.$enum"
    }
    else -> list.map { "$QUOTE$it$QUOTE" }
}

const val QUOTE = "\""

private fun Trigger.classname(): ClassName =
    this::class.asClassName()

private fun WorkflowDispatch?.toKotlin(): CodeBlock {
    if (this == null) return CodeBlock.of("")
    val trigger = CodeBlock.of("%T(", WorkflowDispatch::class)

    val inputsBlock = inputs.joinToCode(
        prefix = CodeBlock.of("mapOf(\n"),
        ifEmpty = CodeBlock.of("),\n"),
        postfix = "))",
    ) { name, input ->
        workflowDispatchInput(name, input)
    }

    return CodeBlock { builder ->
        builder.add(trigger)
        builder.add(inputsBlock)
    }
}

fun workflowDispatchInput(name: String, input: WorkflowDispatch.Input) = CodeBlock { builder ->
    builder
        .add("%S to %T(\n", name, WorkflowDispatch.Input::class)
        .indent()
        .add("type = %M,\n", enumMemberName(input.type))
        .add("description = %S,\n", input.description)
        .add("default = %S,\n", input.default)
        .add("required = %L,\n", input.required)
        .add(
            input.options.joinToCode(
                ifEmpty = CodeBlock.EMPTY,
                prefix = CodeBlock.of("options = %M(", Members.listOf),
                postfix = "),",
                separator = ", ",
                transform = { CodeBlock.of("%S", it) },
            ),
        )
        .unindent()
        .add("),\n")
}

private fun List<ScheduleValue>?.toKotlin(): CodeBlock {
    return this?.joinToCode(
        prefix = CodeBlock.of("%T(listOf(\n", Schedule::class),
        postfix = ")),",
    ) {
        CodeBlock.of("%T(%S)", Cron::class, it.cron)
    } ?: CodeBlock.EMPTY
}

val allTriggers: List<Trigger> = listOf(
    BranchProtectionRule(),
    CheckRun(),
    CheckSuite(),
    Create(),
    Delete(),
    Deployment(),
    DeploymentStatus(),
    Discussion(),
    DiscussionComment(),
    Fork(),
    Gollum(),
    IssueComment(),
    Issues(),
    Label(),
    Milestone(),
    PageBuild(),
    Project(),
    ProjectCard(),
    ProjectColumn(),
    PublicWorkflow(),
    PullRequest(),
    PullRequestReview(),
    PullRequestReviewComment(),
    PullRequestTarget(),
    Push(),
    RegistryPackage(),
    Release(),
    RepositoryDispatch(),
    Schedule(emptyList()),
    Status(),
    Watch(),
    WorkflowCall(),
    WorkflowDispatch(),
    WorkflowRun(),
)

val allTriggersNames: List<String> =
    allTriggers.map { it.triggerName() }

val allTriggersMap: Map<String, Trigger> =
    allTriggersNames.zip(allTriggers).toMap()

val rootProject = File(".").canonicalFile.let {
    when (it.name) {
        "github-workflows-kt" -> it
        "" -> it // Root directory in Docker container.
        else -> it.parentFile.parentFile
    }
}

const val PACKAGE = "io.github.typesafegithub.workflows"
