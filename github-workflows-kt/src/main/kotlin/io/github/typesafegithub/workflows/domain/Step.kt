package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import kotlinx.serialization.Contextual

@Suppress("LongParameterList")
public sealed class Step<out OUTPUTS : Outputs>(
    public open val id: String,
    public open val env: Map<String, String> = mapOf(),
    public open val condition: String? = null,
    public open val continueOnError: Boolean? = null,
    public open val timeoutMinutes: Int? = null,
    public open val outputs: OUTPUTS,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : HasCustomArguments {
    public inner class Conclusion : AbstractResult("steps.$id.conclusion")

    public inner class Outcome : AbstractResult("steps.$id.outcome")

    public val conclusion: Conclusion get() = Conclusion()
    public val outcome: Outcome get() = Outcome()
}

public data class CommandStep(
    override val id: String,
    val name: String? = null,
    val command: String,
    override val env: Map<String, String> = mapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    val shell: Shell? = null,
    val workingDirectory: String? = null,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : Step<Outputs>(
        id = id,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        env = env,
        outputs = Outputs(id),
        _customArguments = _customArguments,
    )

public data class KotlinLogicStep(
    override val id: String,
    val name: String? = null,
    val command: String,
    override val env: Map<String, String> = mapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    val shell: Shell? = null,
    val workingDirectory: String? = null,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
    val logic: Contexts.() -> Unit,
) : Step<Outputs>(
        id = id,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        env = env,
        outputs = Outputs(id),
        _customArguments = _customArguments,
    )

@Suppress("LongParameterList")
public open class ActionStep<out OUTPUTS : Outputs>(
    override val id: String,
    public open val name: String? = null,
    public open val action: Action<OUTPUTS>,
    override val env: Map<String, String> = mapOf(),
    override val condition: String? = null,
    override val continueOnError: Boolean? = null,
    override val timeoutMinutes: Int? = null,
    override val outputs: OUTPUTS,
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : Step<OUTPUTS>(
        id = id,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        env = env,
        outputs = outputs,
        _customArguments = _customArguments,
    )
