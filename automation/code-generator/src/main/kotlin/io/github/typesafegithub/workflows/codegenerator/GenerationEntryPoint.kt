package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.buildActionClassName
import io.github.typesafegithub.workflows.actionbindinggenerator.generateBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.toKotlinPackageName
import io.github.typesafegithub.workflows.actionsmetadata.bindingsToGenerate
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.TypingsSource
import io.github.typesafegithub.workflows.actionsmetadata.model.Version
import io.github.typesafegithub.workflows.codegenerator.types.deleteActionTypesYamlCacheIfObsolete
import io.github.typesafegithub.workflows.codegenerator.types.provideTypes
import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import io.github.typesafegithub.workflows.metadatareading.deleteActionYamlCacheIfObsolete
import io.github.typesafegithub.workflows.metadatareading.prettyPrint
import java.nio.file.Path
import java.nio.file.Paths

/***
 * Either run this main() function or run this command: ./gradlew :code-generator:run
 */
fun main() {
    val listOfBindingsInDocs = Paths.get("docs/supported-actions.md")

    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()
    listOfBindingsInDocs.toFile().delete()
    generateEventPayloads()
    generateBindings()
    generateListOfBindingsForDocs(listOfBindingsInDocs)
}

private fun generateBindings() {
    deleteActionYamlCacheIfObsolete()
    deleteActionTypesYamlCacheIfObsolete()

    bindingsToGenerate.forEach { actionBindingRequest ->
        println("Generating ${actionBindingRequest.actionCoords.prettyPrint}")
        val inputTypings = actionBindingRequest.provideTypes()
        val (code, path) = actionBindingRequest.actionCoords.generateBinding(inputTypings)
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
}

private fun generateListOfBindingsForDocs(listOfBindingsInDocs: Path) {
    listOfBindingsInDocs.toFile().printWriter().use { writer ->
        writer.println(
            """
            This is a complete list of actions for which the library provides typed bindings, grouped by owners. If your
            action is not on the list, see [Using actions](user-guide/using-actions.md) section.

            Click on a version to see the binding's code.

            ## Action bindings

            """.trimIndent(),
        )

        bindingsToGenerate
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

        val uniqueActionsCount = bindingsToGenerate.groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }.size
        val uniqueActionsProvidingTypingsCount = bindingsToGenerate
            .groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }
            .mapValues { (_, versions) -> versions.maxByOrNull { Version(it.actionCoords.version) } }
            .count { (_, actionBindingRequest) -> actionBindingRequest?.typingsSource == TypingsSource.ActionTypes }

        writer.println(
            """

            ## Statistics

            Number of bindings available:

            * counting by actions: $uniqueActionsCount
            * counting each version separately: ${bindingsToGenerate.size}

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
