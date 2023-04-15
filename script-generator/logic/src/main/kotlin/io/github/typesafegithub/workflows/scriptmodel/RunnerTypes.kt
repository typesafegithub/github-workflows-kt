package io.github.typesafegithub.workflows.scriptmodel

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.scriptgenerator.CodeBlock
import io.github.typesafegithub.workflows.scriptgenerator.normalizeEnum
import io.github.typesafegithub.workflows.scriptgenerator.objectsOfSealedClass
import io.github.typesafegithub.workflows.scriptgenerator.orExpression
import io.github.typesafegithub.workflows.yaml.toYaml

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
