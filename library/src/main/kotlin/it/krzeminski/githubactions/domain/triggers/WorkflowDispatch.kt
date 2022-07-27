package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
@Serializable
data class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {

    @Serializable
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

    @Serializable
    class Input(
        val description: String,
        val required: Boolean,
        val type: Type,
        val options: List<String> = emptyList(),
        val default: String? = null,
    )
}
