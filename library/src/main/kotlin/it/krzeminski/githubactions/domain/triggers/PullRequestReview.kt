package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request_review
 */
data class PullRequestReview(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
