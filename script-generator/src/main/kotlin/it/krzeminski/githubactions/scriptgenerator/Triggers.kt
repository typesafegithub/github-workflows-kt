package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.scriptmodel.ScheduleValue
import it.krzeminski.githubactions.scriptmodel.YamlTrigger
import it.krzeminski.githubactions.scriptmodel.YamlWorkflowTriggers
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import it.krzeminski.githubactions.yaml.MapOfYaml
import it.krzeminski.githubactions.yaml.toMap
import it.krzeminski.githubactions.yaml.triggerClassMap

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
        .add(milestone.toKotlin("milestone"))
        .add(page_build.toKotlin("page_build"))
        .add(project.toKotlin("project"))
        .add(project_card.toKotlin("project_card"))
        .add(project_column.toKotlin("project_column"))
        .add(pull_request_review_comment.toKotlin("pull_request_review_comment"))
        .add(registry_package.toKotlin("registry_package"))
        .add(release.toKotlin("release"))
        .add(status.toKotlin("status"))
        .add(watch.toKotlin("watch"))
        .add(workflow_call.toKotlin("workflow_call"))
        .add(workflow_run.toKotlin("workflow_run"))
        .add("),\n").unindent()
}

private fun YamlTrigger?.toKotlin(triggerName: String): CodeBlock {
    return if (this == null) {
        CodeBlock.of("")
    } else {
        val classname = triggerClassMap
            .firstOrNull { it.first == triggerName }
            ?.second
            ?.asClassName()
            ?: error("Couldn't find class for triggerName=$triggerName")

        CodeBlock { builder ->
            builder.add("%T()", classname)
            if (types.isNullOrEmpty()) {
                builder.add(",\n")
            } else {
                builder
                    .indent()
                    .add(
                        ".types(%L),\n",
                        types.joinToString(", ") { CodeBlock.of("%S", it).toString() }
                    )
                    .unindent()
            }
        }
    }
}

fun Trigger?.toKotlin(): CodeBlock {
    if (this == null) {
        return CodeBlock.of("")
    }
    val map: MapOfYaml = this.toMap()
    val builder = CodeBlock.builder()
    builder
        .add("%T(", classname()).indent()

    if (map.all { it.value.isNullOrEmpty() }.not()) {
        builder.add("\n")
    }
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

private fun WorkflowDispatch?.toKotlin(): CodeBlock {
    if (this == null) return CodeBlock.of("")

    val builder = CodeBlock.builder()
        .add("%T(", WorkflowDispatch::class)

    val inputs = this.inputs

    if (inputs.isEmpty()) {
        builder.add("),\n")
    } else {
        builder.add("mapOf(\n")
            .indent()
        inputs.forEach { (name, input) ->
            builder.add("%S to %T(\n", name, WorkflowDispatch.Input::class)
                .indent()
                .add("type = %M,\n", enumMemberName(input.type))
                .add("description = %S,\n", input.description)
                .add("default = %S,\n", input.default)
                .add("required = %L,\n", input.required)
                .unindent()
                .add("),\n")
        }
        builder
            .unindent()
            .add(")")
            .add(")\n")
    }
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
