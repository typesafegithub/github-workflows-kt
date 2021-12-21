package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action

sealed interface Step

data class CommandStep(
    val name: String,
    val command: String,
) : Step

data class ExternalActionStep(
    val name: String,
    val action: Action,
) : Step
