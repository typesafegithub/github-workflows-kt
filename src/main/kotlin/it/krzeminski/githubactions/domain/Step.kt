package it.krzeminski.githubactions.domain

interface Step

data class CommandStep(
    val name: String,
    val command: String,
) : Step
