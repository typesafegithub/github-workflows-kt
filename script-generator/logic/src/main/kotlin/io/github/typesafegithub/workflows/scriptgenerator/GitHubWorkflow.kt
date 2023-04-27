package io.github.typesafegithub.workflows.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflow
import io.github.typesafegithub.workflows.wrappergenerator.generation.toPascalCase
import java.nio.file.Paths

fun YamlWorkflow.toFileSpec(filenameFromUrl: String?, packageName: String = "") =
    FileSpec.builder(packageName, "$name.main.kts")
        .addImport("$PACKAGE.yaml", "toYaml", "writeToFile")
        .addImport("$PACKAGE.dsl.expressions", "expr")
        .addProperty(workFlowProperty(filenameFromUrl))
        .build()

fun YamlWorkflow.workFlowProperty(filenameFromUrl: String?): PropertySpec {
    val filename = (filenameFromUrl ?: name).lowercase().replace(" ", "-")

    return PropertySpec.builder("workflow${filename.toPascalCase()}", Workflow::class)
        .initializer(
            CodeBlock { builder ->
                builder.add("%M(\n", Members.workflow)
                    .indent()
                    .add("name = %S,\n", name)
                    .add("on = %L", on.toKotlin())
                    .add("sourceFile = %T.get(%S),\n", Paths::class, Paths.get(".github/workflows/$filename.main.kts"))
                    .add(workflowEnv())
                    .add(concurrencyOf(concurrency))
                    .unindent()
                    .add(") {\n")
                    .indent()
                    .add(generateJobs())
                    .unindent()
                    .add("}")
            },
        )
        .build()
}

fun concurrencyOf(concurrency: Concurrency?): CodeBlock = when (concurrency) {
    null -> CodeBlock.EMPTY
    else -> CodeBlock.of(
        "concurrency = %T(group = %S, cancelInProgress = %L),\n",
        Concurrency::class.asClassName(),
        concurrency.group,
        concurrency.cancelInProgress,
    )
}

private fun YamlWorkflow.workflowEnv(): CodeBlock {
    return env.joinToCode(
        prefix = CodeBlock.of("env = %M(\n", Members.linkedMapOf),
        postfix = "),",
    ) { key, value ->
        CodeBlock { builder ->
            builder
                .add("%S to ", key)
                .add(value.orExpression())
                .add(",\n")
        }
    }
}

fun YamlWorkflow.toKotlin(filename: String): String = """
        |#!/usr/bin/env kotlin
        |
        |@file:DependsOn("io.github.typesafegithub:github-workflows-kt:$LIBRARY_VERSION")
        |
        |${toFileSpec(filename)}
        |workflow${filename.toPascalCase()}.writeToFile()
""".trimMargin()
