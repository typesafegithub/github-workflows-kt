package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.FreeYamlArgs

data class PullRequest(
    val types: List<Type> = emptyList(),
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
) : Trigger() {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()
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
