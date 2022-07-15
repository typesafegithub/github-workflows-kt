package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs
import it.krzeminski.githubactions.domain.*

@Suppress("LongParameterList")
@GithubActionsDsl
class JobBuilder(
    val id: String,
    val name: String?,
    val runsOn: RunnerType,
    val defaults: Defaults? = null,
    val needs: List<Job>,
    val env: LinkedHashMap<String, String>,
    val condition: String?,
    val strategyMatrix: Map<String, List<String>>?,
    val timeoutMinutes: Int? = null,
    val concurrency: Concurrency? = null,
    override val _customArguments: Map<String, CustomValue>,
) : HasCustomArguments {
    private var job = Job(
        id = id,
        name = name,
        runsOn = runsOn,
        defaults = defaults,
        needs = needs,
        condition = condition,
        env = env,
        steps = emptyList(),
        strategyMatrix = strategyMatrix,
        timeoutMinutes = timeoutMinutes,
        concurrency = concurrency,
        _customArguments = _customArguments,
    )

    fun run(
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): CommandStep = run(
        name = null,
        command = command,
        env = env,
        condition = condition,
        _customArguments = _customArguments,
    )

    fun run(
        name: String? = null,
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): CommandStep {
        val newStep = CommandStep(
            id = "step-${job.steps.size}",
            name = name,
            command = command,
            env = env,
            condition = condition,
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun uses(
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): ExternalActionStep = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
        _customArguments = _customArguments,
    )

    fun uses(
        name: String? = null,
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): ExternalActionStep {
        val newStep = ExternalActionStep(
            id = "step-${job.steps.size}",
            name = name,
            action = action,
            env = env,
            condition = condition,
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun <T> uses(
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): ExternalActionStepWithOutputs<T> = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
        _customArguments = _customArguments,
    )

    fun <T> uses(
        name: String? = null,
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, CustomValue> = mapOf(),
    ): ExternalActionStepWithOutputs<T> {
        val stepId = "step-${job.steps.size}"
        val newStep = ExternalActionStepWithOutputs(
            id = stepId,
            name = name,
            action = action,
            env = env,
            condition = condition,
            outputs = action.buildOutputObject(stepId),
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun build() = job
}
