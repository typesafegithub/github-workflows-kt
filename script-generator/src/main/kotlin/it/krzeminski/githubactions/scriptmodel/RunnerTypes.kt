package it.krzeminski.githubactions.scriptmodel

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.scriptgenerator.CodeBlock
import it.krzeminski.githubactions.scriptgenerator.normalizeEnum
import it.krzeminski.githubactions.scriptgenerator.objectsOfSealedClass
import it.krzeminski.githubactions.scriptgenerator.orExpression
import it.krzeminski.githubactions.yaml.toYaml

val allRunnerTypes: List<RunnerType> by lazy {
    objectsOfSealedClass()
}

fun runnerTypeBlockOf(runsOn: String): CodeBlock {
    val runnerType = allRunnerTypes.firstOrNull { normalizeEnum(it.toYaml()) == normalizeEnum(runsOn) }
        ?: RunnerType.UbuntuLatest.takeIf { runsOn.isBlank() }
    return if (runnerType != null) {
        CodeBlock.of("runsOn = %T,\n", runnerType::class.asClassName())
    } else {
        CodeBlock { builder ->
            builder.add(CodeBlock.of("runsOn = %T(", RunnerType.Custom::class.asClassName()))
                .add(runsOn.orExpression())
                .add(CodeBlock.of("),\n"))
        }
    }
}
