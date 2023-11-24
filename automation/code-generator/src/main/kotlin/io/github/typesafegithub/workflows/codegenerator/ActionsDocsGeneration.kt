package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.TypingActualSource
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.Version
import java.nio.file.Paths

internal fun generateListOfBindingsForDocs(requestsAndBindings: List<Pair<ActionBindingRequest, ActionBinding>>) {
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
                                .joinToString(", ") { it.toMarkdownLinkToKotlinCode(it.second.packageName, it.second.className) }
                        writer.println("    * ${versions.first().first.actionCoords.toMarkdownLinkGithub()} - $kotlinClasses")
                    }
            }

        val uniqueActionsCount =
            requestsAndBindings
                .map { it.first }
                .groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }.size
        val uniqueActionsProvidingTypingsCount =
            requestsAndBindings
                .groupBy { "${it.first.actionCoords.owner}/${it.first.actionCoords.name}" }
                .mapValues { (_, versions) -> versions.maxByOrNull { Version(it.first.actionCoords.version) } }
                .count { (_, requestAndBinding) -> requestAndBinding?.second?.typingActualSource == TypingActualSource.ACTION }

        writer.println(
            """

            ## Statistics

            Number of bindings available:

            * counting by actions: $uniqueActionsCount
            * counting each version separately: ${requestsAndBindings.size}

            Actions [providing typings](https://github.com/typesafegithub/github-actions-typing/) (marked with ✅ on the above list): $uniqueActionsProvidingTypingsCount
            """.trimIndent(),
        )
    }
}

internal fun deleteListOfBindingsInDocs() {
    listOfBindingsInDocs.toFile().delete()
}

private val listOfBindingsInDocs = Paths.get("docs/supported-actions.md")

private fun Pair<ActionBindingRequest, ActionBinding>.toMarkdownLinkToKotlinCode(
    packageName: String,
    className: String,
): String {
    val typingsMarker = if (this.second.typingActualSource == TypingActualSource.ACTION) " ✅" else ""
    return "${this.first.actionCoords.version}$typingsMarker: [`$className`](" +
        "https://github.com/typesafegithub/github-workflows-kt/blob/v[[ version ]]/library/src/gen/kotlin/io/github/" +
        "typesafegithub/workflows/actions/$packageName/$className.kt)"
}

private fun ActionCoords.toMarkdownLinkGithub() =
    "[$name](https://github.com/$owner/${name.substringBefore(
        '/',
    )}${if ("/" in name) "/tree/$version/${name.substringAfter('/')}" else ""})"
