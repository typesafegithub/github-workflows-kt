package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action

sealed class Step(
    open val env: LinkedHashMap<String, String> = linkedMapOf(),
    open val condition: String? = null,
)

data class CommandStep(
    val name: String,
    val command: String,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
) : Step(condition = condition, env = env)

data class ExternalActionStep(
    val name: String,
    val action: Action,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
) : Step(condition = condition, env = env)
