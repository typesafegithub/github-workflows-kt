package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_call
 */
data class WorkflowCall(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
