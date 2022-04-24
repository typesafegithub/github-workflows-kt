package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs
import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments

sealed class Step(
    open val id: String,
    open val env: LinkedHashMap<String, String> = linkedMapOf(),
    open val condition: String? = null,
    override val _customArguments: Map<String, CustomValue> = emptyMap(),
) : HasCustomArguments

interface WithOutputs<T> {
    val outputs: T
}

data class CommandStep(
    override val id: String,
    val name: String? = null,
    val command: String,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val _customArguments: Map<String, CustomValue> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    env = env,
    _customArguments = _customArguments,
)

open class ExternalActionStep(
    override val id: String,
    open val name: String? = null,
    open val action: Action,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val _customArguments: Map<String, CustomValue> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    env = env,
    _customArguments = _customArguments,
)

data class ExternalActionStepWithOutputs<T>(
    override val id: String,
    override val name: String? = null,
    override val action: ActionWithOutputs<T>,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val outputs: T,
    override val _customArguments: Map<String, CustomValue> = emptyMap(),
) : ExternalActionStep(
    name = name,
    action = action,
    id = id,
    condition = condition,
    env = env,
    _customArguments = _customArguments,
),
    WithOutputs<T>
