package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#issues
 */
data class Issues(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
