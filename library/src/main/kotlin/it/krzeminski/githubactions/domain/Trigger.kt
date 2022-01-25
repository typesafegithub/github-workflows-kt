package it.krzeminski.githubactions.domain

sealed class Trigger {
    object Push : Trigger()
    object WorkflowDispatch : Trigger()
    object PullRequest : Trigger()
    data class Schedule(
        val triggers: List<Cron>,
    ) : Trigger()
}

data class Cron(val expression: String)
