package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequest(
    val types: List<EventType> = emptyList(),
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger() {
    init {
        require(!(branches != null && branchesIgnore != null)) {
            "Cannot define both 'branches' and 'branchesIgnore'!"
        }
        require(!(paths != null && pathsIgnore != null)) {
            "Cannot define both 'paths' and 'pathsIgnore'!"
        }
    }

    /**
     * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request
     */
    @OptIn(InternalSerializationApi::class)
    @Serializable
    public sealed class EventType(
        public val name: String,
    ) {
        public object Assigned : EventType("assigned")

        public object Unassigned : EventType("unassigned")

        public object Labeled : EventType("labeled")

        public object Unlabeled : EventType("unlabeled")

        public object Opened : EventType("opened")

        public object Edited : EventType("edited")

        public object Closed : EventType("closed")

        public object Reopened : EventType("reopened")

        public object Synchronize : EventType("synchronized")

        public object ConvertedToDraft : EventType("convert_to_draft")

        public object ReadyForReview : EventType("ready_for_review")

        public object Locked : EventType("locked")

        public object Unlocked : EventType("unlocked")

        public object ReviewRequested : EventType("review_requested")

        public object ReviewRequestRemoved : EventType("review_request_removed")

        public object AutoMergeEnabled : EventType("auto_merge")

        public object AutoMergeDisabled : EventType("auto_merge_disabled")

        public data class Custom(
            val value: String,
        ) : EventType(name = value)
    }
}
