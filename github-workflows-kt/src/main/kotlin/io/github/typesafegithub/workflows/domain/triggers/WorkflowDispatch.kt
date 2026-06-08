package io.github.typesafegithub.workflows.domain.triggers

import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget.EventType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onworkflow_dispatchinputs
@Serializable
public data class WorkflowDispatch(
    val inputs: Map<String, Input> = emptyMap(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @Serializable
    public class Input(
        public val description: String,
        public val required: Boolean,
        public val type: Type,
        public val options: List<String> = emptyList(),
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
}
