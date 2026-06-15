package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class Issues(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Opened : EventType("opened")

        public object Edited : EventType("edited")

        public object Deleted : EventType("deleted")

        public object Transferred : EventType("transferred")

        public object Pinned : EventType("pinned")

        public object Unpinned : EventType("unpinned")

        public object Closed : EventType("closed")

        public object Reopened : EventType("reopened")

        public object Assigned : EventType("assigned")

        public object Unassigned : EventType("unassigned")

        public object Labeled : EventType("labeled")

        public object Unlabeled : EventType("unlabeled")

        public object Locked : EventType("locked")

        public object Unlocked : EventType("unlocked")

        public object Milestoned : EventType("milestoned")

        public object Demilestoned : EventType("demilestoned")

        public object Typed : EventType("typed")

        public object Untyped : EventType("untyped")

        public object FieldAdded : EventType("field_added")

        public object FieldRemoved : EventType("field_removed")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
