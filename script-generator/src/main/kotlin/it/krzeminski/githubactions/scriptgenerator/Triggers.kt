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
    this ?: return CodeBlock.EMPTY

    val classname = triggerClassMap
        .firstOrNull { it.first == triggerName }
        ?.second
        ?.asClassName()
        ?: error("Couldn't find class for triggerName=$triggerName")

    val typesCodeblock = (this.types ?: emptyList())
        .joinToCodeBlock(
            prefix = CodeBlock.of(".types(", classname),
            postfix = CodeBlock.of("),"),
            separator = CodeBlock.of(", "),
            ifEmpty = CodeBlock.of(",\n"),
            newLineAtEnd = true
        ) { type ->
            CodeBlock.of("%S", type)
        }

    return CodeBlock { builder ->
        builder.add("%T()", classname)
            .add(typesCodeblock)
    }
}

fun Trigger?.toKotlin(): CodeBlock {
    if (this == null) {
        return CodeBlock.of("")
    }
    val map: MapOfYaml = this.toMap()
    return map.joinToCodeBlock(
        prefix = CodeBlock.of("%T(", classname()),
        postfix = CodeBlock.of("),"),
        ifEmpty = CodeBlock.of("%T(),\n", classname()),
        newLineAtEnd = true,
    ) { key, value ->
        stringsOrEnums(key, value)?.let { list ->
            CodeBlock.of(
                "%N = %L",
                key.toCamelCase(),
                list.joinToString(separator = ", ", prefix = "listOf(", postfix = ")")
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

private fun Trigger.classname(): ClassName =
    this::class.asClassName()

private fun WorkflowDispatch?.toKotlin(): CodeBlock {
    if (this == null) return CodeBlock.of("")
    val trigger = CodeBlock.of("%T(", WorkflowDispatch::class)

    val inputsBlock = inputs.joinToCodeBlock(
        prefix = CodeBlock.of("mapOf(\n"),
        ifEmpty = CodeBlock.of("),\n"),
        postfix = CodeBlock.of("))")
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
        .unindent()
        .add("),\n")
}

private fun List<ScheduleValue>?.toKotlin(): CodeBlock {
    return this?.joinToCodeBlock(
        prefix = CodeBlock.of("%T(listOf(\n", Schedule::class),
        postfix = CodeBlock.of(")),")
    ) {
        CodeBlock.of("%T(%S)", Cron::class, it.cron)
    } ?: CodeBlock.EMPTY
}
