package it.krzeminski.githubactions.domain

data class Job(
    val name: String,
    val runsOn: RunnerType,
    val needs: List<Job>,
    val steps: List<Step>,
)
