package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import java.nio.file.Paths

fun GithubWorkflow.toFileSpec(filenameFromUrl: String?) = FileSpec.builder("", "$name.main.kts")
    .addImport("$PACKAGE.yaml", "toYaml")
    .addImport("$PACKAGE.dsl", "expr")
    .addProperty(workFlowProperty(filenameFromUrl))
    .build()

fun GithubWorkflow.workFlowProperty(filenameFromUrl: String?): PropertySpec {
    val filename = (filenameFromUrl ?: name).lowercase().replace(" ", "-")

    return PropertySpec.builder("workflow${filename.toPascalCase()}", Workflow::class)
        .initializer(
            CodeBlock { builder ->
                builder.add("%M(\n", Members.workflow)
                    .indent()
                    .add("name = %S,\n", name)
                    .add("on = %L", on.toKotlin())
                    .add("sourceFile = %T.get(%S),\n", Paths::class, Paths.get("$filename.main.kts"))
                    .add("targetFile = %T.get(%S),\n", Paths::class, Paths.get("$filename.yml"))
                    .add(workflowEnv())
                    .unindent()
                    .add(") {\n")
                    .indent()
                    .add(generateJobs())
                    .unindent()
                    .add("}")
                    .add(printlnGenerateYaml())
            }
        )
        .build()
}

private fun GithubWorkflow.workflowEnv() = CodeBlock { builder ->
    if (env.isEmpty()) return@CodeBlock
    builder.add("env = %M(\n", Members.linkedMapOf)
    builder.indent()
    for ((key, value) in env) {
        val (template, arg) = value.orExpression()
        builder.add("%S to $template,\n", key, arg)
    }
    builder.unindent()
    builder.add("),\n")
}

fun GithubWorkflow.toKotlin(filenameFromUrl: String?): String = """
        #!/usr/bin/env kotlin
        
        @file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:$LIBRARY_VERSION")
        
        ${toFileSpec(filenameFromUrl)}
""".trimIndent()

fun printlnGenerateYaml(): CodeBlock = CodeBlock.of(
    """
    .also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
    """.trimIndent()
)
