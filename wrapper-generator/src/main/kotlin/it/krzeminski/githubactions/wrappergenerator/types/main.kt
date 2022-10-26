package it.krzeminski.githubactions.wrappergenerator.types

import it.krzeminski.githubactions.wrappergenerator.domain.TypingsSource
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate
import java.nio.file.Path

private val actionsDir = Path.of("actions")

fun main() {
    actionsDir.toFile().deleteRecursively()

    wrappersToGenerate
        .forEach { it.generateActionTypesYaml() }

    println("All action types generated")
}

fun WrapperRequest.generateActionTypesYaml() {
    println("Generating for ${this.actionCoords}")
    val baseDir = this.pathToActionDataDir()
    baseDir.toFile().mkdirs()
    if (typingsSource == TypingsSource.ActionTypes) {
        baseDir.resolve("typings-hosted-by-action").toFile().createNewFile()
    } else {
        val metadataFile = Path.of("build").resolve("action-yaml")
            .resolve("${this.actionCoords.owner}-${this.actionCoords.name}-${this.actionCoords.version}.yml").toFile()
        baseDir.resolve("action-types.yml").toFile().writeText(toYaml(metadataFile))
    }
}

private fun WrapperRequest.pathToActionDataDir(): Path = with(actionCoords) {
    actionsDir.resolve(owner).resolve(name).resolve(version)
}
