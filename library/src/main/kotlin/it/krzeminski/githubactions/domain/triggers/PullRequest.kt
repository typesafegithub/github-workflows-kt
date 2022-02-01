package it.krzeminski.githubactions.domain.triggers

data class PullRequest(
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
) : Trigger()
