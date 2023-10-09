package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generateBinding
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
    val requestsAndBindings = generateBindings()
    generateListOfBindingsForDocs(requestsAndBindings, listOfBindingsInDocs)
}

private fun generateBindings(): List<Pair<ActionBindingRequest, ActionBinding>> {
    deleteActionYamlCacheIfObsolete()
    deleteActionTypesYamlCacheIfObsolete()

    val requestsAndBindings =
        bindingsToGenerate.map { actionBindingRequest ->
            println("Generating ${actionBindingRequest.actionCoords.prettyPrint}")
            val inputTypings = actionBindingRequest.provideTypes()
            val binding = actionBindingRequest.actionCoords.generateBinding(inputTypings)
            Pair(actionBindingRequest, binding)
        }
    requestsAndBindings.forEach { (_, binding) ->
        with(Paths.get(binding.filePath).toFile()) {
            parentFile.mkdirs()
            writeText(binding.kotlinCode)
        }
    }
    return requestsAndBindings
}

private fun generateListOfBindingsForDocs(
    requestsAndBindings: List<Pair<ActionBindingRequest, ActionBinding>>,
    listOfBindingsInDocs: Path,
) {
    listOfBindingsInDocs.toFile().printWriter().use { writer ->
        writer.println(
            """
            This is a complete list of actions for which the library provides typed bindings, grouped by owners. If your
            action is not on the list, see [Using actions](user-guide/using-actions.md) section.

            Click on a version to see the binding's code.

            ## Action bindings

            """.trimIndent(),
        )

        requestsAndBindings
            .groupBy { it.first.actionCoords.owner }
            .forEach { (owner, ownedRequestsAndBindings) ->
                writer.println("* $owner")
                ownedRequestsAndBindings
                    .groupBy { it.first.actionCoords.name }
                    .forEach { (_, versions) ->
                        val kotlinClasses =
                            versions
                                .sortedBy { Version(it.first.actionCoords.version) }
                                .joinToString(", ") { it.first.toMarkdownLinkToKotlinCode(it.second.packageName, it.second.className) }
                        writer.println("    * ${versions.first().first.actionCoords.toMarkdownLinkGithub()} - $kotlinClasses")
                    }
            }

        val uniqueActionsCount = bindingsToGenerate.groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }.size
        val uniqueActionsProvidingTypingsCount =
            bindingsToGenerate
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

private fun ActionBindingRequest.toMarkdownLinkToKotlinCode(
    packageName: String,
    className: String,
): String {
    val typingsMarker = if (typingsSource == TypingsSource.ActionTypes) " ✅" else ""
    return "${actionCoords.version}$typingsMarker: [`$className`](" +
        "https://github.com/typesafegithub/github-workflows-kt/blob/v[[ version ]]/library/src/gen/kotlin/io/github/" +
        "typesafegithub/workflows/actions/$packageName/$className.kt)"
}

private fun ActionCoords.toMarkdownLinkGithub() =
    "[$name](https://github.com/$owner/${name.substringBefore(
        '/',
    )}${if ("/" in name) "/tree/$version/${name.substringAfter('/')}" else ""})"
