package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.buildActionClassName
import io.github.typesafegithub.workflows.actionbindinggenerator.deleteActionYamlCacheIfObsolete
import io.github.typesafegithub.workflows.actionbindinggenerator.generateWrapper
import io.github.typesafegithub.workflows.actionbindinggenerator.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.toKotlinPackageName
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.TypingsSource
import io.github.typesafegithub.workflows.actionsmetadata.model.Version
import io.github.typesafegithub.workflows.actionsmetadata.wrappersToGenerate
import io.github.typesafegithub.workflows.codegenerator.types.deleteActionTypesYamlCacheIfObsolete
import io.github.typesafegithub.workflows.codegenerator.types.provideTypes
import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import java.nio.file.Path
import java.nio.file.Paths

/***
 * Either run this main() function or run this command: ./gradlew :code-generator:run
 */
fun main() {
    val listOfWrappersInDocs = Paths.get("docs/supported-actions.md")

    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()
    listOfWrappersInDocs.toFile().delete()
    generateEventPayloads()
    generateWrappers()
    generateListOfWrappersForDocs(listOfWrappersInDocs)
}

private fun generateWrappers() {
    deleteActionYamlCacheIfObsolete()
    deleteActionTypesYamlCacheIfObsolete()

    wrappersToGenerate.forEach { wrapperRequest ->
        println("Generating ${wrapperRequest.actionCoords.prettyPrint}")
        val inputTypings = wrapperRequest.provideTypes()
        val (code, path) = wrapperRequest.actionCoords.generateWrapper(inputTypings)
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
}

private fun generateListOfWrappersForDocs(listOfWrappersInDocs: Path) {
    listOfWrappersInDocs.toFile().printWriter().use { writer ->
        writer.println(
            """
            This is a complete list of actions for which the library provides typed wrappers, grouped by owners. If your
            action is not on the list, see [Using actions](user-guide/using-actions.md) section.

            Click on a version to see the wrapper's code.

            ## Wrappers

            """.trimIndent(),
        )

        wrappersToGenerate
            .groupBy { it.actionCoords.owner }
            .forEach { (owner, ownedActions) ->
                writer.println("* $owner")
                ownedActions
                    .groupBy { it.actionCoords.name }
                    .forEach { (_, versions) ->
                        val kotlinClasses = versions
                            .sortedBy { Version(it.actionCoords.version) }
                            .joinToString(", ") { it.toMarkdownLinkToKotlinCode() }
                        writer.println("    * ${versions.first().actionCoords.toMarkdownLinkGithub()} - $kotlinClasses")
                    }
            }

        val uniqueActionsCount = wrappersToGenerate.groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }.size
        val uniqueActionsProvidingTypingsCount = wrappersToGenerate
            .groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }
            .mapValues { (_, versions) -> versions.maxByOrNull { Version(it.actionCoords.version) } }
            .count { (_, wrapperRequest) -> wrapperRequest?.typingsSource == TypingsSource.ActionTypes }

        writer.println(
            """

            ## Statistics

            Number of wrappers available:

            * counting by actions: $uniqueActionsCount
            * counting each version separately: ${wrappersToGenerate.size}

            Actions [providing typings](https://github.com/typesafegithub/github-actions-typing/) (marked with ✅ on the above list): $uniqueActionsProvidingTypingsCount
            """.trimIndent(),
        )
    }
}

private fun ActionBindingRequest.toMarkdownLinkToKotlinCode(): String {
    val typingsMarker = if (typingsSource == TypingsSource.ActionTypes) " ✅" else ""
    return "${actionCoords.version}$typingsMarker: [`${actionCoords.buildActionClassName()}`](https://github.com/typesafegithub/github-workflows-kt/blob/v[[ version ]]/library/src/gen/kotlin/io/github/typesafegithub/workflows/actions/${actionCoords.owner.toKotlinPackageName()}/${this.actionCoords.buildActionClassName()}.kt)"
}

private fun ActionCoords.toMarkdownLinkGithub() =
    "[$name](https://github.com/$owner/${name.substringBefore('/')}${if ("/" in name) "/tree/$version/${name.substringAfter('/')}" else ""})"
