package it.krzeminski.githubactions.domain.triggers

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
data class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
) : Trigger() {

    enum class Type {
        Choice,
        Environment,
        Boolean,
        String,
    }

    class Input(
        val description: String,
        val required: Boolean,
        val type: Type,
        val options: List<String> = emptyList(),
        val default: String? = null,
    )
}
