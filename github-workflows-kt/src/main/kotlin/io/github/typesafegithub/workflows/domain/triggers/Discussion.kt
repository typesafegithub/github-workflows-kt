package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class Discussion(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Created : EventType("created")

        public object Edited : EventType("edited")

        public object Deleted : EventType("deleted")

        public object Transferred : EventType("transferred")

        public object Pinned : EventType("pinned")

        public object Unpinned : EventType("unpinned")

        public object Labeled : EventType("labeled")

        public object Unlabeled : EventType("unlabeled")

        public object Locked : EventType("locked")

        public object Unlocked : EventType("unlocked")

        public object CategoryChanged : EventType("category_changed")

        public object Answered : EventType("answered")

        public object Unanswered : EventType("unanswered")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
