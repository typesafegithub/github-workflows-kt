package io.github.typesafegithub.workflows.actionbindinggenerator

import kotlinx.serialization.decodeFromString

public fun extractUsedActionsFromWorkflow(manifest: String): List<ActionCoords> {
    val parsedWorkflow = parseWorkflow(manifest)
    val usesStrings = extractUsesStrings(parsedWorkflow)
    return usesStrings
        .map { it.toActionCoords() }
        .also { assertSingleVersionForEachAction(it) }
}

private fun extractUsesStrings(parsedWorkflow: Workflow) =
    parsedWorkflow.jobs?.flatMap {
        it.value.steps.mapNotNull { step ->
            step.uses
        }
    } ?: emptyList()

private fun parseWorkflow(manifest: String) =
    try {
        myYaml.decodeFromString<Workflow>(manifest)
    } catch (e: Throwable) {
        throw IllegalArgumentException("The YAML is invalid: ${e.message}")
    }

private fun assertSingleVersionForEachAction(actionCoords: List<ActionCoords>) {
    val actionsWithMultipleVersions =
        actionCoords
            .groupBy { "${it.owner}/${it.name}" }
            .mapValues { it.value.toSet().size }
            .filterValues { it > 1 }
            .keys
    if (actionsWithMultipleVersions.isNotEmpty()) {
        throw IllegalArgumentException("Multiple versions defined for actions: $actionsWithMultipleVersions")
    }
}
