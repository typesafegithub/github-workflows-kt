package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#repository_dispatch
 */
data class RepositoryDispatch(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
