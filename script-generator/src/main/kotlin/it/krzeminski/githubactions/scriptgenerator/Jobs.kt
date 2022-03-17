package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.scriptmodel.YamlJob
import it.krzeminski.githubactions.scriptmodel.YamlStep
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate

fun YamlWorkflow.generateJobs() = CodeBlock { builder ->
    jobs.forEach { (name, job) ->
        builder.add(
            "job(%S, %M",
            name,
            enumMemberName<RunnerType>(job.runsOn) ?: enumMemberName(RunnerType.UbuntuLatest)
        )
        builder.add(job.env.joinToCode(
            ifEmpty = CodeBlock.EMPTY,
            prefix = CodeBlock.of(",\nenv = %M(", Members.linkedMapOf),
            postfix = ")\n",
            transform = { key, value -> CodeBlock.of("%S to %S", key, value) }
        ))
        builder.add(job.customArguments())
        builder.add(") {\n")
        builder.indent()
        job.steps.forEach { step ->
            if (step.uses != null) {
                val coords = ActionCoords(step.uses)
                val availableWrappers = wrappersToGenerate.filter {
                    it.actionCoords.copy(version = "") == coords.copy(version = "")
                }
                val wrapper: WrapperRequest? = availableWrappers.firstOrNull {
                    it.actionCoords.buildActionClassName() == coords.buildActionClassName()
                } ?: availableWrappers.maxByOrNull { it.actionCoords.version }

                builder.add(step.generateAction(wrapper?.actionCoords ?: coords, wrapper?.inputTypings))
            } else {
                builder.add(step.generateCommand())
            }
        }
        builder.unindent()
            .add("}\n\n")
    }
}

private fun YamlJob.customArguments(): CodeBlock {
    val map = listOfNotNull(
        ("needs" to needs).takeIf { needs.isNotEmpty() },
    ).toMap()
    return map.joinToCode(
        ifEmpty = CodeBlock.EMPTY,
        prefix = CodeBlock.of(", _customArguments = %M(\n", Members.mapOf),
        separator = "",
        postfix = ")",
        transform = { key, list -> list.joinToCode(
            prefix = CodeBlock.of("%S to %T(", key, ListCustomValue::class),
            separator = ", ",
            postfix = "),\n",
            newLineAtEnd = false,
            transform = { CodeBlock.of("%S", it) }
        )
        }
    )
}

fun YamlStep.generateCommand() = CodeBlock { builder ->
    builder
        .add(("run(\n"))
        .indent()
        .add("name = %S,\n", name ?: run)
        .add("command = %S,\n", run)
        .add(
            env.joinToCode(
                prefix = CodeBlock.of("%L = linkedMapOf(\n", "env"),
                postfix = "),",
                ifEmpty = CodeBlock.EMPTY
            ) { key, value ->
                value?.let {
                    val (template, arg) = value.orExpression()
                    CodeBlock.of("%S to $template", key, arg)
                }
            }
        )
    if (condition != null) {
        val (template, arg) = condition.orExpression()
        builder.add("condition = $template,\n", arg)
    }
    builder.unindent().add(")\n")
}
