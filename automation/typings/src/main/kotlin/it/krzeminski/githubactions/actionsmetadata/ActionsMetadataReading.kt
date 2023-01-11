package it.krzeminski.githubactions.actionsmetadata

import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.actionsmetadata.model.ActionCoords
import it.krzeminski.githubactions.actionsmetadata.model.ActionTypes
import it.krzeminski.githubactions.actionsmetadata.model.TypingsSource
import it.krzeminski.githubactions.actionsmetadata.model.WrapperRequest
import it.krzeminski.githubactions.actionsmetadata.model.prettyPrint
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.readText

internal fun readActionsMetadata(): List<WrapperRequest> =
    readLocalActionTypings()
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

private val myYaml = Yaml(
    configuration = Yaml.default.configuration.copy(
        strictMode = false,
    ),
)
