package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import it.krzeminski.githubactions.wrappergenerator.metadata.actionYmlUrl
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import java.nio.file.Paths

/***
 * Either run this main() function and then manually call
 *    ./gradlew ktlintFormat
 *
 * Or do both using this command
 *    ./gradlew :wrapper-generator:run
 */
fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()

    checkDuplicateWrappers()
    println(wrappersToGenerate.suggestDeprecations())

    wrappersToGenerate.forEach { (actionCoords, inputTypings) ->
        println("Generating ${actionCoords.owner}/${actionCoords.name}@${actionCoords.version}...")
        val (code, path) = actionCoords.generateWrapper(inputTypings)
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
    println(
        """Now reformat the code with the command:
        |./gradlew ktlintFormat
    """.trimMargin()
    )
}

fun List<WrapperRequest>.suggestDeprecations(): String {
    val nonDeprecatedCoords = this.map { it.actionCoords }
        .filter { it.deprecatedByVersion == null }
    val groupedBy: List<List<ActionCoords>> = nonDeprecatedCoords
        .groupBy { coords -> "${coords.owner}/${coords.name}" }
        .values.toList()
    val messages = groupedBy.flatMap { list ->
        val maxVersion = list.maxByOrNull { it.version }?.version ?: error("Unexpected empty list in $groupedBy")
        list.filter { it.version != maxVersion }
            .map { coords ->
                """WARNING: newer version available for ${coords.prettyPrint}. Maybe add: deprecatedByVersion = "$maxVersion" ?"""
            }
    }
    return messages.joinToString("\n")
}

private fun checkDuplicateWrappers() {
    val duplicateWrappers =
        wrappersToGenerate.groupBy { it.actionCoords.actionYmlUrl }
            .filterValues { it.size != 1 }
            .keys
    require(duplicateWrappers.isEmpty()) { "Duplicate wrappers requests: $duplicateWrappers" }
}
