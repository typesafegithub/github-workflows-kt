package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.Trigger
import it.krzeminski.githubactions.domain.Workflow
import kotlinx.serialization.encodeToString

fun Workflow.toYaml(): String {
    val yamlWorkflow = YamlWorkflow(
        name = name,
        on = on.toYaml(),
        jobs = jobs.toYaml(),
    )
    return Yaml.default.encodeToString(yamlWorkflow)
}

fun List<Trigger>.toYaml() =
    YamlTriggers(
        workflow_dispatch = if (Trigger.WorkflowDispatch in this) "" else null,
    )

fun List<Job>.toYaml() =
    associate { it.name to YamlJob(name = it.name) }
