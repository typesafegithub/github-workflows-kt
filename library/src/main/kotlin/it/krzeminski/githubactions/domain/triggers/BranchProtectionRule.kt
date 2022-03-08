package it.krzeminski.githubactions.domain.triggers

data class BranchProtectionRule(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
