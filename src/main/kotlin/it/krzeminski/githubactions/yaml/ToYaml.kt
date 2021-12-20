package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
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
    val yaml = Yaml(configuration = Yaml.default.configuration
        .copy(
            encodeDefaults = false,
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "name",
        ))
    return yaml.encodeToString(yamlWorkflow)
}

fun List<Trigger>.toYaml() =
    YamlTriggers(
        workflow_dispatch = if (Trigger.WorkflowDispatch in this) mapOf() else null,
        push = if (Trigger.Push in this) mapOf() else null,
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
                    run = it.command,
                )
            is ExternalActionStep ->
                YamlExternalAction(
                    uses = it.action,
                )
        }
    }

fun RunnerType.toYaml() =
    when (this) {
        UbuntuLatest -> "ubuntu-latest"
    }
