package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#milestone
 */
data class Milestone(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
