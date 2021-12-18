package it.krzeminski.githubactions.domain

data class Workflow(
    val name: String,
    val jobs: List<String>,
)
