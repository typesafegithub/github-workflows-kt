package it.krzeminski.githubactions.domain.triggers

data class Delete(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
