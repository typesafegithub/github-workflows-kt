package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import it.krzeminski.githubactions.wrappergenerator.generation.suggestDeprecations
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName
import it.krzeminski.githubactions.wrappergenerator.metadata.actionYmlUrl
import it.krzeminski.githubactions.wrappergenerator.metadata.deleteActionYamlCacheIfObsolete
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import java.nio.file.Path
import java.nio.file.Paths

/***
 * Either run this main() function and then manually call
 *    ./gradlew ktlintFormat
 *
 * Or do both using this command
 *    ./gradlew :wrapper-generator:run
 */
fun main() {
    checkDuplicateWrappers()
    checkWrappersOrder()
    println(wrappersToGenerate.suggestDeprecations())

    val listOfWrappersInDocs = Paths.get("docs/supported-actions.md")

    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()
    listOfWrappersInDocs.toFile().delete()

    generateWrappers()
    generateListOfWrappersForDocs(listOfWrappersInDocs)
}

private fun generateWrappers() {
    deleteActionYamlCacheIfObsolete()
    wrappersToGenerate.forEach { (actionCoords, inputTypings) ->
        println("Generating ${actionCoords.prettyPrint}")
        val (code, path) = actionCoords.generateWrapper(inputTypings)
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
    println(
        """
            |Now reformat the code with the command:
            |./gradlew ktlintFormat
        """.trimMargin()
    )
}

private fun generateListOfWrappersForDocs(listOfWrappersInDocs: Path) {
    listOfWrappersInDocs.toFile().printWriter().use { writer ->
        writer.println(
            """
            This is a complete list of actions for which the library provides typed wrappers, grouped by owners. If your
            action is not on the list, see [Using actions](user-guide/using-actions.md) section.

            Click on a version to see the wrapper's code.

            ## Wrappers

            """.trimIndent()
        )

        wrappersToGenerate
            .map { it.actionCoords }
            .groupBy { it.owner }
            .forEach { (owner, ownedActions) ->
                writer.println("* $owner")
                ownedActions
                    .groupBy { it.name }
                    .forEach { (_, versions) ->
                        val kotlinClasses = versions.joinToString(", ") { it.toMarkdownLinkToKotlinCode() }
                        writer.println("    * ${versions.first().toMarkdownLinkGithub()} - $kotlinClasses")
                    }
            }

        writer.println(
            """

            ## Statistics

            Number of wrappers available:

            * counting by actions: ${wrappersToGenerate.groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }.size}
            * counting each version separately: ${wrappersToGenerate.size}
            """.trimIndent()
        )
    }
}

private fun ActionCoords.toMarkdownLinkToKotlinCode() =
    "$version: [`${buildActionClassName()}`](https://github.com/krzema12/github-actions-kotlin-dsl/tree/main/library/src/gen/kotlin/it/krzeminski/githubactions/actions/${owner.toKotlinPackageName()}/${this.buildActionClassName()}.kt)"

private fun ActionCoords.toMarkdownLinkGithub() =
    "[$name](https://github.com/$owner/$name)"

private fun checkDuplicateWrappers() {
    val duplicateWrappers =
        wrappersToGenerate.groupBy { it.actionCoords.actionYmlUrl }
            .filterValues { it.size != 1 }
            .keys
    require(duplicateWrappers.isEmpty()) { "Duplicate wrappers requests: $duplicateWrappers" }
}

private fun checkWrappersOrder() {
    val sortedWrappers = wrappersToGenerate.sortedBy { it.actionCoords.actionYmlUrl.lowercase() }
    require(wrappersToGenerate == sortedWrappers) {
        val firstNonMatchingRequest = (wrappersToGenerate zip sortedWrappers).first { it.first != it.second }
        """Please sort the wrappers according to their owner, name and version.
           First non-matching request:
           - in current: ${firstNonMatchingRequest.first.actionCoords}
           - in desired: ${firstNonMatchingRequest.second.actionCoords}
        """
    }
}
