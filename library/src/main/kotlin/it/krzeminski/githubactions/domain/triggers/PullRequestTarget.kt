package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue

// https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request_target
data class PullRequestTarget(
    val types: List<Type> = emptyList(),
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger() {

    init {
        require(!(branches != null && branchesIgnore != null)) {
            "Cannot define both 'branches' and 'branchesIgnore'!"
        }
        require(!(paths != null && pathsIgnore != null)) {
            "Cannot define both 'paths' and 'pathsIgnore'!"
        }
    }

    enum class Type {
        Assigned,
        Unassigned,
        Labeled,
        Unlabeled,
        Opened,
        Edited,
        Closed,
        Reopened,
        Synchronize,
        ConvertedToDraft,
        ReadyForReview,
        Locked,
        Unlocked,
        ReviewRequested,
        ReviewRequestRemoved,
        AutoMergeEnabled,
        AutoMergeDisabled,
    }
}
