package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import it.krzeminski.githubactions.wrappergenerator.metadata.actionYmlUrl
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

    wrappersToGenerate.forEach { (actionCoords, inputTypings) ->
        println("Generating $actionCoords.prettyPrint")
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

private fun checkDuplicateWrappers() {
    val duplicateWrappers =
        wrappersToGenerate.groupBy { it.actionCoords.actionYmlUrl }
            .filterValues { it.size != 1 }
            .keys
    require(duplicateWrappers.isEmpty()) { "Duplicate wrappers requests: $duplicateWrappers" }
}
