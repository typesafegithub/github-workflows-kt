package io.github.typesafegithub.workflows.actionbindinggenerator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString

// TODO: cover edge cases in scope of https://github.com/typesafegithub/github-workflows-kt/issues/941
//  See TODOs in unit tests.
public fun extractUsedActionsFromWorkflow(manifest: String): List<ActionCoords> {
    val myYaml =
        Yaml(
            configuration =
                Yaml.default.configuration.copy(
                    strictMode = false,
                ),
        )
    val parsedWorkflow = try {
        myYaml.decodeFromString<Workflow>(manifest)
    } catch (e: Throwable) {
        throw IllegalArgumentException("The YAML is invalid: ${e.message}")
    }
    val usesStrings =
        parsedWorkflow.jobs?.flatMap {
            it.value.steps.mapNotNull { step ->
                step.uses
            }
        } ?: emptyList()

    val actionCoords = usesStrings.map { it.toActionCoords() }

    val actionsWithMultipleVersions = actionCoords
        .groupingBy { "${it.owner}/${it.name}" }
        .eachCount()
        .filterValues { it > 1 }
        .keys
    if (actionsWithMultipleVersions.isNotEmpty()) {
        throw IllegalArgumentException("Multiple versions defined for actions: $actionsWithMultipleVersions")
    }

    return actionCoords
}

private fun String.toActionCoords(): ActionCoords {
    val (owner, name, version) = this.split('/', '@')
    return ActionCoords(
        owner = owner,
        name = name,
        version = version,
    )
}
