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
    sourceFile: Path,
    targetFile: Path,
    jobs: List<Job> = emptyList(),
) {
    private var workflow = Workflow(
        name = name,
        on = on,
        sourceFile = sourceFile,
        targetFile = targetFile,
        jobs = jobs,
    )

    fun job(
        name: String,
        runsOn: RunnerType,
        needs: List<Job> = emptyList(),
        strategyMatrix: Map<String, List<String>>? = null,
        block: JobBuilder.() -> Unit,
    ): Job {
        val jobBuilder = JobBuilder(
            name = name,
            runsOn = runsOn,
            needs = needs,
            strategyMatrix = strategyMatrix,
        )
        jobBuilder.block()
        val newJob = jobBuilder.build()
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

fun workflow(
    name: String,
    on: List<Trigger>,
    sourceFile: Path,
    targetFile: Path,
    block: WorkflowBuilder.() -> Unit,
): Workflow {
    val workflowBuilder = WorkflowBuilder(
        name = name,
        on = on,
        sourceFile = sourceFile,
        targetFile = targetFile,
    )
    workflowBuilder.block()
    return workflowBuilder.build()
}
