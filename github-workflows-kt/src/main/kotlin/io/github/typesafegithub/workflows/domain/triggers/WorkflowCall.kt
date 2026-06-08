package io.github.typesafegithub.workflows.domain.triggers

import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget.EventType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_call
 */
@Serializable
public data class WorkflowCall(
    val inputs: Map<String, Input> = emptyMap(),
    val outputs: Map<String, Output>? = null,
    val secrets: Map<String, Secret>? = null,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @Serializable
    public class Input(
        public val description: String,
        public val required: Boolean,
        public val type: Type,
        public val default: String? = null,
    ) {
        @Serializable
        public sealed class Type(
            public val name: kotlin.String,
        ) {
            public object Choice : Type("choice")

            public object Environment : Type("environment")

            public object Boolean : Type("boolean")

            public object Number : Type("number")

            public object String : Type("string")

            public data class Custom(
                val value: kotlin.String,
            ) : EventType(name = value)
        }
    }

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
