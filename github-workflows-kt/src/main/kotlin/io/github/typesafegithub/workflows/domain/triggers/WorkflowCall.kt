package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_call
 */
@Serializable
public data class WorkflowCall(
    val inputs: Map<String, Input> = emptyMap(),
    val outputs: Map<String, Output> = emptyMap(),
    val secrets: Map<String, Secret> = emptyMap(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @Serializable
    public enum class Type {
        @SerialName("boolean")
        Boolean,

        @SerialName("number")
        Number,

        @SerialName("string")
        String,
    }

    @Serializable
    public class Input(
        public val description: String,
        public val required: Boolean,
        public val type: Type,
        public val default: String? = null,
    )

    @Serializable
    public class Output(
        public val description: String,
        public val value: String,
    )

    @Serializable
    public class Secret(
        public val description: String,
        public val required: Boolean,
    )
}
