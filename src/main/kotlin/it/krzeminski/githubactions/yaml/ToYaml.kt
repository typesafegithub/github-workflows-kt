package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.Checkout
import it.krzeminski.githubactions.actions.DownloadArtifact
import it.krzeminski.githubactions.actions.FetchDepth
import it.krzeminski.githubactions.actions.UploadArtifact
import it.krzeminski.githubactions.actions.YamlCheckoutArguments
import it.krzeminski.githubactions.actions.YamlDownloadArtifactArguments
import it.krzeminski.githubactions.actions.YamlUploadArtifactArguments
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
            breakScalarsAt = 99999,
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
        strategy = it.strategyMatrix?.toYaml() ?: YamlStrategy(),
    ) }

fun Map<String, List<String>>.toYaml() =
    YamlStrategy(
        matrix = this,
    )

fun List<Step>.toYaml() =
    map {
        when (it) {
            is CommandStep ->
                YamlRunStep(
                    name = it.name,
                    run = it.command,
                    `if` = it.condition,
                )
            is ExternalActionStep ->
                YamlExternalAction(
                    uses = it.action.name,
                    with = it.action.toYamlArguments(),
                    `if` = it.condition,
                )
        }
    }

fun RunnerType.toYaml() =
    when (this) {
        UbuntuLatest -> "ubuntu-latest"
    }

fun Action.toYamlArguments() =
    when (this) {
        is Checkout -> YamlCheckoutArguments(
            fetchDepth = when (fetchDepth) {
                FetchDepth.Infinite -> 0
                is FetchDepth.Quantity -> fetchDepth.value
                null -> 1
            },
        )
        is UploadArtifact -> YamlUploadArtifactArguments(
            name = artifactName,
            path = path.joinToString(separator = "\n"),
        )
        is DownloadArtifact -> YamlDownloadArtifactArguments(
            name = artifactName,
            path = path,
        )
    }
