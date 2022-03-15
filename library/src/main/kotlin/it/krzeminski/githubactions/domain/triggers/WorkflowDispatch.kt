package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
    override val _customArguments: Map<String, CustomValue> = mapOf(),
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
