package it.krzeminski.githubactions.wrappergenerator.types

import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.metadata.actionYamlDir
import it.krzeminski.githubactions.wrappergenerator.metadata.gitHubUrl
import it.krzeminski.githubactions.wrappergenerator.metadata.yamlName
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate
import java.io.File

private val actionTypesDir = File("generated-action-types")

fun main() {
    actionTypesDir.deleteRecursively()
    actionTypesDir.mkdir()
    for (wrapper in wrappersToGenerate) {
        wrapper.generateActionTypes()
    }
    println("All action types generated")
    val readme = actionTypesDir.resolve("README.md").canonicalFile
    readme.writeText(generateReadme(wrappersToGenerate))
    println("See $readme")
}

fun WrapperRequest.generateActionTypes() {
    println("Generating ${actionTypesDir.resolve(fileName())} from ${actionYamlDir.resolve(fileName())}")
    val metadataFile = actionYamlDir.resolve(fileName())
    actionTypesDir.resolve(fileName()).writeText(toYaml(metadataFile))
}

fun generateReadme(wrappersToGenerate: List<WrapperRequest>): String {
    val lines = wrappersToGenerate.joinToString(separator = "") {
        val yamlName = it.actionCoords.yamlName
        "| [$yamlName](${it.fileName()}) | ${it.actionCoords.gitHubUrl}|\n"
    }

    return """
        # Types for GitHub Actions

        See https://github.com/krzema12/github-actions-typing

        | File         | Repository|
        |--------------|-----------|
    """.trimIndent() + "\n" + lines
}

private fun WrapperRequest.fileName(): String = with(actionCoords) {
    "$owner-$name-$version.yml"
}
