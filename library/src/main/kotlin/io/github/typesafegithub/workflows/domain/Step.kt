package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import kotlinx.serialization.Contextual

public sealed class Step(
    public open val id: String,
    public open val env: LinkedHashMap<String, String> = linkedMapOf(),
    public open val condition: String? = null,
    public open val continueOnError: Boolean? = null,
    public open val timeoutMinutes: Int? = null,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : HasCustomArguments

public interface WithOutputs<out T> {
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
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    continueOnError = continueOnError,
    timeoutMinutes = timeoutMinutes,
    env = env,
    _customArguments = _customArguments,
)

@Suppress("LongParameterList")
public open class ExternalActionStep<out OUTPUTS : Outputs>(
    override val id: String,
    public open val name: String? = null,
    public open val action: Action<OUTPUTS>,
    override val env: LinkedHashMap<String, String> = linkedMapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    override val outputs: OUTPUTS,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : Step(
    id = id,
    condition = condition,
    continueOnError = continueOnError,
    timeoutMinutes = timeoutMinutes,
    env = env,
    _customArguments = _customArguments,
),
    WithOutputs<OUTPUTS>
