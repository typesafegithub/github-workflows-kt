package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Step
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
    associate { it.name to YamlJob(
        runsOn = it.runsOn.toYaml(),
        steps = it.steps.toYaml(),
    ) }

fun List<Step>.toYaml() =
    map {
        when (it) {
            is CommandStep ->
                YamlRunStep(
                    name = it.name,
                    run = it.command,
                )
        }
    }

fun RunnerType.toYaml() =
    when (this) {
        UbuntuLatest -> "ubuntu-latest"
    }
