package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.actions.Checkout
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Step
import it.krzeminski.githubactions.domain.Trigger
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.dsl.toBuilder
import kotlinx.serialization.encodeToString
import java.nio.file.Path

fun Workflow.toYaml(addConsistencyCheck: Boolean = true): String {
    val jobsWithConsistencyCheck = if (addConsistencyCheck) {
        val consistencyCheckJob = this.toBuilder().job(
            name = "check_yaml_consistency",
            runsOn = UbuntuLatest,
        ) {
            uses("Check out", Checkout())
            run("Install Kotlin", "sudo snap install --classic kotlin")
            run("Consistency check", "diff -u '$targetFile' <('$sourceFile')")
        }
        listOf(consistencyCheckJob) + jobs.map {
            it.copy(needs = listOf(consistencyCheckJob))
        }
    } else {
        jobs
    }
    val yamlWorkflow = YamlWorkflow(
        name = name,
        on = on.toYaml(),
        jobs = jobsWithConsistencyCheck.toYaml(),
    )
    val yamlOutput = yaml.encodeToString(yamlWorkflow)
    return yamlOutput.postprocess(sourceFile)
}

fun Workflow.writeToFile() {
    val yaml = this.toYaml(
        // Because the current consistency check logic relies on writing to standard output.
        addConsistencyCheck = false,
    )
    this.targetFile.toFile().writeText(yaml)
}

private val yaml = Yaml(
    configuration = Yaml.default.configuration
        .copy(
            encodeDefaults = false,
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "polymorphic_type",
            breakScalarsAt = 99999,
        )
)

private fun String.postprocess(sourceFile: Path) =
    (
        """
        # This file was generated using Kotlin DSL ($sourceFile).
        # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
        
        
        """.trimIndent() + this
        )
        // Ideally, kaml should be able to output no info about polymorphic type.
        .replace(Regex("polymorphic_type.*"), "")

fun List<Trigger>.toYaml() =
    YamlTriggers(
        workflow_dispatch = if (Trigger.WorkflowDispatch in this) mapOf() else null,
        push = if (Trigger.Push in this) mapOf() else null,
    )

fun List<Job>.toYaml() =
    associate {
        it.name to YamlJob(
            runsOn = it.runsOn.toYaml(),
            steps = it.steps.toYaml(),
            needs = it.needs.map { it.name },
            strategy = it.strategyMatrix?.toYaml() ?: YamlStrategy(),
        )
    }

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
