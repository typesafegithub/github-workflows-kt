package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestReview(
    val types: List<EventType> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Submitted : EventType("submitted")

        public object Edited : EventType("edited")

        public object Dismissed : EventType("dismissed")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
