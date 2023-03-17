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
import kotlin.streams.asSequence

internal fun readActionsMetadata(): List<WrapperRequest> =
    readLocalActionTypings()
        .addDeprecationInfo()
        .sortedBy { it.actionCoords.prettyPrint.lowercase() }

private fun readLocalActionTypings(): List<WrapperRequest> {
    val actionTypingsDirectory = Path.of("actions")

    return Files.walk(actionTypingsDirectory).asSequence()
        .filter { it.isRegularFile() }
        .filter { it.name !in setOf("commit-hash.txt") }
        .map {
            val pathParts = it.toFile().invariantSeparatorsPath.split("/")
            // pathParts[0] is "actions" directory
            val owner = pathParts[1]
            val name = pathParts[2]
            val version = pathParts[3]
            val subname = pathParts.subList(4, pathParts.size - 1).joinToString("/")
            val file = pathParts.last()
            WrapperRequest(
                actionCoords = ActionCoords(
                    owner = owner,
                    name = listOfNotNull(name, subname.ifEmpty { null }).joinToString("/"),
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
        // Don't allow any unknown keys, to keep the YAMLs minimal.
        strictMode = true,
    ),
)
