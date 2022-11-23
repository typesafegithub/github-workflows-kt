package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.TypingsSource
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.metadata.myYaml
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import it.krzeminski.githubactions.wrappergenerator.types.ActionTypes
import it.krzeminski.githubactions.wrappergenerator.types.toTypesMap
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.readText

val wrappersToGenerate = readLocalActionTypings()
    .addDeprecationInfo()
    .sortedBy { it.actionCoords.prettyPrint.lowercase() }

private fun readLocalActionTypings(): List<WrapperRequest> {
    val actionTypingsDirectory = Path.of("actions")

    return Files.walk(actionTypingsDirectory)
        .filter { it.isRegularFile() }
        .filter { it.name !in setOf("commit-hash.txt") }
        .map {
            val (_, owner, name, version, file) = it.toString().split("/")
            WrapperRequest(
                actionCoords = ActionCoords(
                    owner = owner,
                    name = name,
                    version = version,
                    deprecatedByVersion = null, // It's set in postprocessing.
                ),
                typingsSource = buildTypingsSource(
                    path = it,
                    fileName = file,
                    actionTypingsDirectory = actionTypingsDirectory,
                ),
            )
        }.toList()
}

private fun buildTypingsSource(
    path: Path,
    fileName: String,
    actionTypingsDirectory: Path,
) = when (fileName) {
    "action-types.yml" -> {
        val typings = try {
            myYaml.decodeFromString<ActionTypes>(path.readText())
        } catch (e: Exception) {
            println("There was a problem parsing action typing: $path")
            throw e
        }
        TypingsSource.WrapperGenerator(inputTypings = typings.toTypesMap())
    }
    "typings-hosted-by-action" -> TypingsSource.ActionTypes
    else -> error("An unexpected file found in $actionTypingsDirectory: '$fileName'")
}
