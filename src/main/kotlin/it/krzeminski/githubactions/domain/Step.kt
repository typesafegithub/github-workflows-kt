package it.krzeminski.githubactions.domain

sealed interface Step

data class CommandStep(
    val name: String,
    val command: String,
) : Step
