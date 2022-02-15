package it.krzeminski.githubactions.domain.triggers

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
) : Trigger() {

    @Suppress("EnumNaming", "EnumEntryName")
    enum class Type {
        choice,
        environment,
        boolean,
        string,
    }

    class Input(
        val description: String,
        val required: Boolean,
        val type: Type,
        val options: List<String> = emptyList(),
        val default: String? = null,
    )
}
