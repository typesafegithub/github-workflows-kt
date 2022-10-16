package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs
import it.krzeminski.githubactions.dsl.HasCustomArguments
import kotlinx.serialization.Contextual

public sealed class Step(
    public open val id: String,
    public open val env: LinkedHashMap<String, String> = linkedMapOf(),
    public open val condition: String? = null,
    public open val continueOnError: Boolean? = null,
    public open val timeoutMinutes: Int? = null,
    override val _customArguments: Map<String, @Contextual Any> = emptyMap(),
) : HasCustomArguments

public interface WithOutputs<T> {
    public val outputs: T
}

public data class CommandStep(
    override val id: String,
    val name: String? = null,
    val command: String,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    val shell: Shell? = null,
    val workingDirectory: String? = null,
    override val _customArguments: Map<String, @Contextual Any> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    continueOnError = continueOnError,
    timeoutMinutes = timeoutMinutes,
    env = env,
    _customArguments = _customArguments,
)

@Suppress("LongParameterList")
public open class ExternalActionStep(
    override val id: String,
    public open val name: String? = null,
    public open val action: Action,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    override val _customArguments: Map<String, @Contextual Any> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    continueOnError = continueOnError,
    timeoutMinutes = timeoutMinutes,
    env = env,
    _customArguments = _customArguments,
)

public data class ExternalActionStepWithOutputs<T>(
    override val id: String,
    override val name: String? = null,
    override val action: ActionWithOutputs<T>,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    override val outputs: T,
    override val _customArguments: Map<String, @Contextual Any> = emptyMap(),
) : ExternalActionStep(
    name = name,
    action = action,
    id = id,
    condition = condition,
    continueOnError = continueOnError,
    timeoutMinutes = timeoutMinutes,
    env = env,
    _customArguments = _customArguments,
),
    WithOutputs<T>
