package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class Project(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Created : EventType("created")

        public object Updated : EventType("updated")

        public object Closed : EventType("closed")

        public object Reopened : EventType("reopened")

        public object Edited : EventType("edited")

        public object Deleted : EventType("deleted")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
