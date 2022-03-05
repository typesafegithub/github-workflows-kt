package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.SerialName

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
@kotlinx.serialization.Serializable
class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
) : Trigger() {

    @kotlinx.serialization.Serializable
    enum class Type {
        @SerialName("choice")
        Choice,
        @SerialName("environment")
        Environment,
        @SerialName("boolean")
        Boolean,
        @SerialName("string")
        String,
    }

    @kotlinx.serialization.Serializable
    class Input(
        val description: String,
        val required: Boolean,
        val type: Type,
        val options: List<String> = emptyList(),
        val default: String? = null,
    )
}
