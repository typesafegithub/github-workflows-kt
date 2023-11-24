package io.github.typesafegithub.workflows.actionsmetadata

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.prettyPrint
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.TypingsSource
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.streams.asSequence

internal fun readActionsMetadata(): List<ActionBindingRequest> =
    readLocalActionTypings()
        .addDeprecationInfo()
        .sortedBy { it.actionCoords.prettyPrint.lowercase() }

private fun readLocalActionTypings(): List<ActionBindingRequest> {
    val actionTypingsDirectory = Path.of("actions")

    return Files.walk(actionTypingsDirectory).asSequence()
        .filter { it.isRegularFile() }
        .filter { it.name !in setOf("commit-hash.txt", "action") }
        .map {
            val pathParts = it.toFile().invariantSeparatorsPath.split("/")
            // pathParts[0] is "actions" directory
            val owner = pathParts[1]
            val name = pathParts[2]
            val version = pathParts[3]
            val subname = pathParts.subList(4, pathParts.size - 1).joinToString("/")
            val file = pathParts.last()
            ActionBindingRequest(
                actionCoords =
                    ActionCoords(
                        owner = owner,
                        name = listOfNotNull(name, subname.ifEmpty { null }).joinToString("/"),
                        version = version,
                        // It's set in postprocessing.
                        deprecatedByVersion = null,
                    ),
                typingsSource =
                    buildTypingsSource(
                        fileName = file,
                        actionTypingsDirectory = actionTypingsDirectory,
                    ),
            )
        }.toList()
}

private fun buildTypingsSource(
    fileName: String,
    actionTypingsDirectory: Path,
) = when (fileName) {
    "action-types.yml" -> {
        // TODO: the empty map is just a placeholder value - the typings are fetched in another place.
        //  It should be eventually removed.
        TypingsSource.CodeGenerator(inputTypings = emptyMap())
    }
    "typings-hosted-by-action" -> TypingsSource.ActionTypes
    else -> error("An unexpected file found in $actionTypingsDirectory: '$fileName'")
}
