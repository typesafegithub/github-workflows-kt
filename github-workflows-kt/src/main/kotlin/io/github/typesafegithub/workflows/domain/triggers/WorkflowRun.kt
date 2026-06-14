package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class WorkflowRun(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Completed : EventType("completed")

        public object Requested : EventType("requested")

        public object InProgress : EventType("in_progress")

        public object Waiting : EventType("waiting")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
