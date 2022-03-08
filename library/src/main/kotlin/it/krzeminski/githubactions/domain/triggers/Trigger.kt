package it.krzeminski.githubactions.domain.triggers

sealed class Trigger {
    abstract val triggerName: String
}
