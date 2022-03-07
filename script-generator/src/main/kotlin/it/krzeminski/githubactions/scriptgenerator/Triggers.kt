package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.scriptmodel.ScheduleValue
import it.krzeminski.githubactions.scriptmodel.WorkflowOn
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

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
    this ?: return CodeBlock.of("")
    val map = this.toMap()
    val builder = CodeBlock.builder()
    builder
        .add("%T(\n", classname()).indent()
    for ((key, value) in map) {
        value ?: continue
        val list: List<String>? = when {
            value !is List<*> -> null
            key == "types" -> when (this) {
                is PullRequest -> value.map { "PullRequest.Type.${it.toString().toPascalCase()}" }
                is PullRequestTarget -> value.map { "PullRequestTarget.Type.${it.toString().toPascalCase()}" }
                else -> error("Unexpected types=$value for key=$key")
            }
            else -> value.map { "$QUOTE$it$QUOTE" }
        }

        builder
            .add(
                "%N = %L,\n", key.toCamelCase(),
                list?.joinToString(separator = ", ", prefix = "listOf(", postfix = ")")
                    ?: "$QUOTE$value$QUOTE"
            )
    }
    builder.unindent().add("),\n")
    return builder.build()
}

private fun Trigger.classname(): ClassName {
    return when (this) {
        is PullRequest -> PullRequest::class.asClassName()
        is PullRequestTarget -> PullRequestTarget::class.asClassName()
        is Push -> Push::class.asClassName()
        is Schedule -> Schedule::class.asClassName()
        is WorkflowDispatch -> WorkflowDispatch::class.asClassName()
    }
}

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

fun Trigger.toMap(): LinkedHashMap<String, Any?> = when (this) {
    is PullRequest -> linkedMapOf(
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
        "types" to types,
    )
    is PullRequestTarget -> linkedMapOf(
        "branches" to branches,
        "branches-ignore" to branchesIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
        "types" to types,
    )
    is Push -> linkedMapOf(
        "branches" to branches,
        "tags" to tags,
        "branches-ignore" to branchesIgnore,
        "tags-ignore" to tagsIgnore,
        "paths" to paths,
        "paths-ignore" to pathsIgnore,
    )
    is Schedule -> linkedMapOf()
    is WorkflowDispatch -> linkedMapOf()
}
