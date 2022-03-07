package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import it.krzeminski.githubactions.scriptmodel.SerializedStep
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate

fun GithubWorkflow.generateJobs() = CodeBlock { builder ->
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
                val wrapper = wrappersToGenerate.firstOrNull {
                    it.actionCoords.buildActionClassName() == coords.buildActionClassName() &&
                        it.actionCoords.owner.lowercase() == coords.owner.lowercase()
                }

                builder.add(generateAction(step, coords, wrapper))
            } else {
                builder.add(generateCommand(step))
            }
        }
        builder.unindent()
            .add("}\n\n")
    }
}

fun generateCommand(step: SerializedStep) = CodeBlock { builder ->
    builder
        .add(("run(\n"))
        .indent()
        .add("name = %S,\n", step.name ?: step.run)
        .add("command = %S,\n", step.run)
        .add(generatePropertyWithLinkedMap("env", step.env))
    if (step.condition != null) {
        val (template, arg) = step.condition.orExpression()
        builder.add("condition = $template,\n", arg)
    }
    builder.unindent().add(")\n")
}
