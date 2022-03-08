package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#issue_comment
 */
data class IssueComment(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
