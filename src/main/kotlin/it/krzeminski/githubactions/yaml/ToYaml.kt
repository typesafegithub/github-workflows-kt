package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.actions.toYamlArguments
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
            polymorphismPropertyName = "polymorphic_type",
        ))
    val yamlOutput = yaml.encodeToString(yamlWorkflow)
    return yamlOutput.postprocess()
}

private fun String.postprocess() =
    // Ideally, kaml should be able to output no info about polymorphic type.
    replace(Regex("polymorphic_type.*"), "")

fun List<Trigger>.toYaml() =
    YamlTriggers(
        workflow_dispatch = if (Trigger.WorkflowDispatch in this) mapOf() else null,
        push = if (Trigger.Push in this) mapOf() else null,
    )

fun List<Job>.toYaml() =
    associate { it.name to YamlJob(
        runsOn = it.runsOn.toYaml(),
        steps = it.steps.toYaml(),
        needs = it.needs.map { it.name },
    ) }

fun List<Step>.toYaml() =
    map {
        when (it) {
            is CommandStep ->
                YamlRunStep(
                    name = it.name,
                    run = it.command,
                )
            is ExternalActionStep ->
                YamlExternalAction(
                    uses = it.action.name,
                    with = it.action.toYamlArguments(),
                )
        }
    }

fun RunnerType.toYaml() =
    when (this) {
        UbuntuLatest -> "ubuntu-latest"
    }
