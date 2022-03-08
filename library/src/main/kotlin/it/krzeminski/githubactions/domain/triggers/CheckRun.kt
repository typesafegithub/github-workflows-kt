package it.krzeminski.githubactions.domain.triggers

data class CheckRun(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
