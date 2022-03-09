package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.domain.triggers.BranchProtectionRule
import it.krzeminski.githubactions.domain.triggers.CheckRun
import it.krzeminski.githubactions.domain.triggers.CheckSuite
import it.krzeminski.githubactions.domain.triggers.Create
import it.krzeminski.githubactions.domain.triggers.Cron
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
import it.krzeminski.githubactions.scriptmodel.ScheduleValue
import it.krzeminski.githubactions.scriptmodel.WorkflowOn
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import it.krzeminski.githubactions.yaml.MapOfYaml
import it.krzeminski.githubactions.yaml.toMap

fun WorkflowOn.toKotlin(): CodeBlock {
    val builder = CodeBlock.builder()
    builder.add("listOf(\n").indent()
    builder.add(push.toKotlin())
    builder.add(pull_request.toKotlin())
    builder.add(pull_request_target.toKotlin())
    builder.add(schedule.toKotlin())
    builder.add(workflow_dispatch?.inputs.toKotlin())
    builder.add("),\n").unindent()
    return builder.build()
}

fun Trigger?.toKotlin(): CodeBlock {
    if (this == null) {
        return CodeBlock.of("")
    }
    val map: MapOfYaml = this.toMap()
    val builder = CodeBlock.builder()
    builder
        .add("%T(\n", classname()).indent()
    for ((key, value) in map) {
        val list: List<String> = stringsOrEnums(key, value) ?: continue
        builder
            .add(
                "%N = %L,\n", key.toCamelCase(),
                list.joinToString(separator = ", ", prefix = "listOf(", postfix = ")")
            )
    }
    builder.unindent().add("),\n")
    return builder.build()
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

private fun Trigger.classname(): ClassName =
    this::class.asClassName()

private fun Map<String, WorkflowDispatch.Input>?.toKotlin(): CodeBlock {
    if (this == null || isEmpty()) return CodeBlock.of("")
    val builder = CodeBlock.builder()
        .add("%T(mapOf(\n", WorkflowDispatch::class)
        .indent()
    forEach { (name, input) ->
        builder.add("%S to %T(\n", name, WorkflowDispatch.Input::class)
            .indent()
            .add("type = %M,\n", enumMemberName(input.type))
            .add("description = %S,\n", input.description)
            .add("default = %S,\n", input.default)
            .add("required = %L,\n", input.required)
            .unindent()
            .add("),\n")
    }
    builder.unindent().add("))\n")
    return builder.build()
}

private fun List<ScheduleValue>?.toKotlin(): CodeBlock {
    if (this == null || isEmpty()) return CodeBlock.of("")
    val builder = CodeBlock.builder()
        .add("%T(listOf(\n", Schedule::class)
        .indent()
    forEach {
        builder.add("%T(%S),\n", Cron::class, it.cron)
    }
    builder.unindent().add(")),\n")
    return builder.build()
}
