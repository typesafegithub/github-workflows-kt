package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Trigger
import java.nio.file.Path

@GithubActionsDsl
class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFile: Path,
    jobs: List<Job> = emptyList(),
) {
    internal var workflow = Workflow(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFile = targetFile,
        jobs = jobs,
    )

    @Suppress("LongParameterList")
    fun job(
        name: String,
        runsOn: RunnerType,
        needs: List<Job> = emptyList(),
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        block: JobBuilder.() -> Unit,
    ): Job {
        val jobBuilder = JobBuilder(
            name = name,
            runsOn = runsOn,
            needs = needs,
            condition = condition,
            env = env,
            strategyMatrix = strategyMatrix,
        )
        jobBuilder.block()
        val newJob = jobBuilder.build()

        require(newJob.steps.isNotEmpty()) {
            "There are no steps defined!"
        }

        workflow = workflow.copy(jobs = workflow.jobs + newJob)
        return newJob
    }

    fun build() = workflow
}

fun Workflow.toBuilder() =
    WorkflowBuilder(
        name = name,
        on = on,
        sourceFile = sourceFile,
        targetFile = targetFile,
        jobs = jobs,
    )

@Suppress("LongParameterList")
fun workflow(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFile: Path,
    block: WorkflowBuilder.() -> Unit,
): Workflow {
    require(on.isNotEmpty()) {
        "There are no triggers defined!"
    }

    val workflowBuilder = WorkflowBuilder(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFile = targetFile,
    )
    workflowBuilder.block()

    require(workflowBuilder.workflow.jobs.isNotEmpty()) {
        "There are no jobs defined!"
    }
    workflowBuilder.workflow.jobs.requireUniqueJobNames()

    return workflowBuilder.build()
}

private fun List<Job>.requireUniqueJobNames() {
    val countPerJobName = this
        .map { it.name }
        .groupBy { it }
        .mapValues { it.value.count() }

    require(countPerJobName.none { it.value > 1 }) {
        val duplicatedJobNames = countPerJobName
            .filter { it.value > 1 }
            .map { it.key }
        "Duplicated job names: $duplicatedJobNames"
    }
}
