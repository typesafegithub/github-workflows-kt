package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectColumn(
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
        public object Moved : EventType("moved")
        public object Deleted : EventType("deleted")
        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
