package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Trigger
import it.krzeminski.githubactions.domain.Workflow

class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
) {
    private var workflow = Workflow(
        name = name,
        on = on,
        jobs = emptyList(),
    )

    fun job(
        name: String,
        runsOn: RunnerType,
        needs: List<Job> = emptyList(),
        block: JobBuilder.() -> Unit,
    ): Job {
        val jobBuilder = JobBuilder(
            name = name,
            runsOn = runsOn,
            needs = needs,
        )
        jobBuilder.block()
        val newJob = jobBuilder.build()
        workflow = workflow.copy(jobs = workflow.jobs + newJob)
        return newJob
    }

    fun build() = workflow
}

fun workflow(
    name: String,
    on: List<Trigger>,
    block: WorkflowBuilder.() -> Unit,
): Workflow {
    val workflowBuilder = WorkflowBuilder(
        name = name,
        on = on,
    )
    workflowBuilder.block()
    return workflowBuilder.build()
}