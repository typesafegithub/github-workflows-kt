package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#project_column
 */
data class ProjectColumn(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
