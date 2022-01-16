package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action

sealed class Step(open val condition: String? = null)

data class CommandStep(
    val name: String,
    val command: String,
    override val condition: String?,
) : Step(condition = condition)

data class ExternalActionStep(
    val name: String,
    val action: Action,
    override val condition: String?,
) : Step(condition = condition)
