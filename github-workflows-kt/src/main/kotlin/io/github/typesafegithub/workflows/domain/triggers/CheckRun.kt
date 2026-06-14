package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class CheckRun(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Created : EventType("created")
        public object Rerequested : EventType("rerequested")
        public object Completed : EventType("completed")
        public object RequestedAction : EventType("requested_action")
        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
