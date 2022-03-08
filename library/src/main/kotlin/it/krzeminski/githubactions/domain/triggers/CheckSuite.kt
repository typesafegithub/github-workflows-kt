package it.krzeminski.githubactions.domain.triggers

data class CheckSuite(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
