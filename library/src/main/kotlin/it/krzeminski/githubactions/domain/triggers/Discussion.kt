package it.krzeminski.githubactions.domain.triggers

data class Discussion(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
