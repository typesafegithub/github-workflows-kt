package it.krzeminski.githubactions.domain

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val jobs: List<Job>,
)
