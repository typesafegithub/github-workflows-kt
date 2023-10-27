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
    val parsedWorkflow = myYaml.decodeFromString<Workflow>(manifest)
    val usesStrings =
        parsedWorkflow.jobs.flatMap {
            it.value.steps.mapNotNull { step ->
                step.uses
            }
        }

    return usesStrings.map { it.toActionCoords() }
}

private fun String.toActionCoords(): ActionCoords {
    val (owner, name, version) = this.split('/', '@')
    return ActionCoords(
        owner = owner,
        name = name,
        version = version,
    )
}
