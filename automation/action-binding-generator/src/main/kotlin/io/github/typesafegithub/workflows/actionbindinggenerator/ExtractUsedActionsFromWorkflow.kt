package io.github.typesafegithub.workflows.actionbindinggenerator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString

internal fun extractUsedActionsFromWorkflow(manifest: String): List<ActionCoords> {
    val myYaml = Yaml(
        configuration =
        Yaml.default.configuration.copy(
            strictMode = false,
        ),
    )
    val parsedWorkflow = myYaml.decodeFromString<Workflow>(manifest)
    val usesStrings = parsedWorkflow.jobs.flatMap {
        it.value.steps.mapNotNull { step ->
            step.uses
        }
    }

    return usesStrings
        .map {
            val (owner, name, version) = it.split('/', '@')
            ActionCoords(
                owner = owner,
                name = name,
                version = version,
            )
        }
}
