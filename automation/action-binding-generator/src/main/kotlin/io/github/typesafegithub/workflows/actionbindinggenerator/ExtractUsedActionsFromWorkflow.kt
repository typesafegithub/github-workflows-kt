package io.github.typesafegithub.workflows.actionbindinggenerator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString

internal fun extractUsedActionsFromWorkflow(manifest: String): List<ActionCoords> {
//    val settings = LoadSettings.builder().build()
//    val load = Load(settings)
//    val loadedYaml: Map<String, Any> = load.loadFromString(manifest) as Map<String, Any>
//    val jobsMap: Map<String, Any> = loadedYaml["jobs"] as Map<String, Any>
//    val jobName = jobsMap.keys.first()
//    val usesStrings: List<String> =
//        ((jobsMap[jobName] as Map<String, Any>)["steps"] as List<Map<String, Any>>)
//            .map { it["uses"] as String }
//    return usesStrings
//        .map {
//            val (owner, name, version) = it.split('/', '@')
//            ActionCoords(
//                owner = owner,
//                name = name,
//                version = version,
//            )
//        }
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
