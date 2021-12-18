package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.Workflow

class WorkflowBuilder(
    name: String,
) {
    private var workflow = Workflow(
        name = name,
        jobs = emptyList(),
    )

    fun job(name: String) {
        workflow = workflow.copy(jobs = workflow.jobs + name)
    }

    fun build() = workflow
}

fun workflow(name: String, block: WorkflowBuilder.() -> Unit): Workflow {
    val workflowBuilder = WorkflowBuilder(
        name = name,
    )
    workflowBuilder.block()
    return workflowBuilder.build()
}