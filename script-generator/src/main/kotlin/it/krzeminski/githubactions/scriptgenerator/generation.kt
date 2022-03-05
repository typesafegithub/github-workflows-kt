package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.scriptmodel.ActionCoords
import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import it.krzeminski.githubactions.scriptmodel.Job
import it.krzeminski.githubactions.scriptmodel.ScheduleValue
import it.krzeminski.githubactions.scriptmodel.SerializedStep
import it.krzeminski.githubactions.scriptmodel.WorkflowOn
import it.krzeminski.githubactions.scriptmodel.classname
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import it.krzeminski.githubactions.wrappergenerator.metadata.actionYamlUrl
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate
import java.nio.file.Paths

const val LIBRARY_VERSION = "0.9.0"

fun GithubWorkflow.toKotlin(): String =
    "#!/usr/bin/env kotlin\n\n${toFileSpec()}"

private const val PACKAGE = "it.krzeminski.githubactions"

fun GithubWorkflow.toFileSpec() = FileSpec.builder("", "$name.main.kts")
    .addAnnotation(
        AnnotationSpec.builder(ClassName("", "DependsOn"))
            .addMember("%S", "it.krzeminski:github-actions-kotlin-dsl:$LIBRARY_VERSION")
            .build()
    )
    .addImport("$PACKAGE.yaml", "toYaml")
    .addImport("$PACKAGE.dsl", "expr")
    .addProperty(workFlowProperty())
    .build()

fun GithubWorkflow.workFlowProperty(): PropertySpec {
    val filename = name.lowercase().replace(" ", "-")
    val initializer = CodeBlock.builder()
        .add("%M(\n", MemberName("$PACKAGE.dsl", "workflow"))
        .indent()
        .add("name = %S,\n", name)
        .add("on = %L", on.toKotlin())
        .add("sourceFile = %T.get(%S),\n", Paths::class, Paths.get("$filename.main.kts"))
        .add("targetFile = %T.get(%S),\n", Paths::class, Paths.get("$filename.yml"))
        .unindent()
        .add(") {\n")
        .indent()
        .addJobs(jobs)
        .unindent()
        .add("}")
        .add(printlnGenerateYaml())
        .build()

    return PropertySpec.builder("workflow${name.toPascalCase()}", it.krzeminski.githubactions.domain.Workflow::class)
        .initializer(initializer)
        .build()
}

fun printlnGenerateYaml(): CodeBlock = CodeBlock.of(
    """
    .also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
    """.trimIndent()
)

fun CodeBlock.Builder.addJobs(jobs: Map<String, Job>): CodeBlock.Builder {
    val builder = this
    jobs.forEach { (name, job) ->
        add("job(%S, %M) {\n", name, enumMemberName<RunnerType>(job.runsOn) ?: enumMemberName(RunnerType.UbuntuLatest))
            .indent()
        job.steps.forEach {
            addStep(it, wrappersToGenerate)
        }
        builder
            .unindent()
            .add("}\n\n")
    }
    return this
}

inline fun <reified T : Enum<T>> enumMemberName(input: T): MemberName? =
    enumMemberName<T>(input.name)

inline fun <reified T : Enum<T>> enumMemberName(input: String): MemberName? {
    return enumValues<T>()
        .firstOrNull { it.name == input.toPascalCase() }
        ?.let { MemberName(T::class.asClassName(), it.name) }
        ?: run {
            println("WARNING: Unexpected enum ${T::class} $input=$input expected=${enumValues<T>()}")
            null
        }
}

private fun CodeBlock.Builder.addStep(
    step: SerializedStep,
    wrappersToGenerate: List<WrapperRequest>,
): CodeBlock.Builder {
    val builder = this

    if (step.uses != null) {
        val coords = ActionCoords(step.uses)
        val wrapper = wrappersToGenerate.firstOrNull { it.actionCoords.actionYamlUrl == coords.actionYamlUrl }
        add("uses(\n")
        indent()
        add("name = %S,\n", step.name ?: coords.buildActionClassName())
        add("action = %T(", coords.classname())

        if (step.with.isEmpty()) {
            builder.add("),\n")
        } else {
            builder.add("\n").indent()
            step.with.forEach { (key, value) ->
                if (value != null) {
                    val typing = wrapper?.inputTypings?.get(key) ?: StringTyping
                    val (percent, arg) = valueWithTyping(value, typing, coords)
                    add("%N = $percent,\n", key.toCamelCase(), arg)
                }
            }
            builder.unindent().add("),\n")
        }
        add(codeBlockOf("env", step.env))
        if (step.condition != null) {
            val (key, arg) = step.condition.orExpression()
            add("condition = $key,\n", arg)
        }
        unindent()
        add(")\n")
    } else {
        builder
            .add(("run(\n"))
            .indent()
            .add("name = %S,\n", step.name ?: step.run)
            .add("command = %S,\n", step.run)
            .add(codeBlockOf("env", step.env))
        if (step.condition != null) {
            val (key, arg) = step.condition.orExpression()
            add("condition = $key,\n", arg)
        }
        builder.unindent().add(")\n")
    }
    return this
}

fun valueWithTyping(value: String, typing: Typing, coords: ActionCoords): Pair<String, String> {
    val classname = coords.buildActionClassName()
    return when (typing) {
        is EnumTyping -> "%L" to "$classname.${typing.typeName}.${value.toPascalCase()}"
        is BooleanTyping -> "%L" to value
        is IntegerWithSpecialValueTyping -> "%L" to "$classname.${typing.typeName}.Value($value)"
        is ListOfTypings -> {
            val delimeter = if (typing.delimiter == "\\n") "\n" else typing.delimiter
            val listString = value.trim().split(delimeter).joinToString(
                prefix = "listOf(", postfix = ")", transform = {
                    val (percent, transformed) = valueWithTyping(it, typing.typing, coords)
                    if (percent == "%S") "$QUOTE$transformed$QUOTE" else transformed
                }
            )
            "%L" to listString
        }
        else -> "%S" to value
    }
}

fun codeBlockOf(property: String, map: LinkedHashMap<String, String?>?): CodeBlock {
    if (map == null || map.all { it.value.isNullOrBlank() }) return CodeBlock.of("")

    val builder = CodeBlock.builder()
    builder
        .add("%L = linkedMapOf(\n", property)
        .indent()
    for ((key, value) in map) {
        if (value != null) {
            val (percent, arg) = value.orExpression()
            builder.add("%S to $percent,\n", key, arg)
        }
    }
    builder.unindent()
        .add("),\n")
    return builder.build()
}

private fun String.orExpression(): Pair<String, String> {
    val input = trim()
    if (input.startsWith(EXPR_PREFIX) && endsWith(EXPR_SUFFIX)) {
        val expression = input.removeSuffix(EXPR_SUFFIX).removePrefix(EXPR_PREFIX).trim()
        return "expr(%S)" to expression
    } else {
        return "%S" to input
    }
}

private const val EXPR_PREFIX = "\${{"
private const val EXPR_SUFFIX = "}}"

private fun WorkflowOn.toKotlin(): CodeBlock {
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

const val QUOTE = "\""

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
