package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
@Serializable
public data class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {

    @Serializable
    public enum class Type {
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
    public class Input(
        public val description: String,
        public val required: Boolean,
        public val type: Type,
        public val options: List<String> = emptyList(),
        public val default: String? = null,
    )
}
