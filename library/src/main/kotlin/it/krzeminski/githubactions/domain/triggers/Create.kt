package it.krzeminski.githubactions.domain.triggers

data class Create(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
