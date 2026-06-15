package io.github.typesafegithub.workflows.domain.triggers

import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget.EventType
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#release
 */
@Serializable
public data class Release(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Published : EventType("published")

        public object Unpublished : EventType("unpublished")

        public object Created : EventType("created")

        public object Edited : EventType("edited")

        public object Deleted : EventType("deleted")

        public object Prereleased : EventType("prerelease")

        public object Released : EventType("released")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
