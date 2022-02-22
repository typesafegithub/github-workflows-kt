package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs

sealed class Step(
    open val id: String,
    open val env: LinkedHashMap<String, String> = linkedMapOf(),
    open val condition: String? = null,
)

interface WithOutputs<T> {
    val outputs: T
}

data class CommandStep(
    override val id: String,
    val name: String,
    val command: String,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
) : Step(id = id, condition = condition, env = env)

open class ExternalActionStep(
    override val id: String,
    open val name: String,
    open val action: Action,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
) : Step(id = id, condition = condition, env = env)

data class ExternalActionStepWithOutputs<T>(
    override val id: String,
    override val name: String,
    override val action: ActionWithOutputs<T>,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val outputs: T,
) : ExternalActionStep(name = name, action = action, id = id, condition = condition, env = env), WithOutputs<T>
