package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.scriptmodel.YamlStep
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate

fun YamlWorkflow.generateJobs() = CodeBlock { builder ->
    jobs.forEach { (name, job) ->
        builder.add(
            "job(%S, %M) {\n",
            name,
            enumMemberName<RunnerType>(job.runsOn) ?: enumMemberName(RunnerType.UbuntuLatest)
        )
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
