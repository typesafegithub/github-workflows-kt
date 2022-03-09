package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PullRequest(
    val types: List<Type> = emptyList(),
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
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
    @kotlinx.serialization.Serializable
    enum class Type {
        @SerialName("assigned")
        Assigned,
        @SerialName("unassigned")
        Unassigned,
        @SerialName("labeled")
        Labeled,
        @SerialName("unlabeled")
        Unlabeled,
        @SerialName("opened")
        Opened,
        @SerialName("edited")
        Edited,
        @SerialName("closed")
        Closed,
        @SerialName("reopened")
        Reopened,
        @SerialName("synchronize")
        Synchronize,
        @SerialName("converted_to_draft")
        ConvertedToDraft,
        @SerialName("ready_for_review")
        ReadyForReview,
        @SerialName("locked")
        Locked,
        @SerialName("unlocked")
        Unlocked,
        @SerialName("review_requested")
        ReviewRequested,
        @SerialName("review_requested_removed")
        ReviewRequestRemoved,
        @SerialName("auto_merge_enabled")
        AutoMergeEnabled,
        @SerialName("auto_merge_disabled")
        AutoMergeDisabled,
    }
}
