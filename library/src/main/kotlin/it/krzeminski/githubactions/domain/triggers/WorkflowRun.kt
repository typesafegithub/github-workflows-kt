package it.krzeminski.githubactions.domain.triggers

/**
 * Need type: https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_run
 */
data class WorkflowRun(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
