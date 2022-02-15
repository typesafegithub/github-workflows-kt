package it.krzeminski.githubactions.domain.triggers

// https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request_target
data class PullRequestTarget(
    val types: List<Type> = emptyList(),
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
) : Trigger() {

    @Suppress("EnumNaming")
    enum class Type {
        assigned,
        unassigned,
        labeled,
        unlabeled,
        opened,
        edited,
        closed,
        reopened,
        synchronize,
        converted_to_draft,
        ready_for_review,
        locked,
        unlocked,
        review_requested,
        review_request_removed,
        auto_merge_enabled,
        auto_merge_disabled,
    }
}
