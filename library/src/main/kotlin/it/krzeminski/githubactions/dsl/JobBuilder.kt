package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.ExternalActionStepWithOutputs
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType

@Suppress("LongParameterList")
@GithubActionsDsl
class JobBuilder(
    val id: String,
    val name: String?,
    val runsOn: RunnerType,
    val needs: List<Job>,
    val env: LinkedHashMap<String, String>,
    val condition: String?,
    val strategyMatrix: Map<String, List<String>>?,
    val timeoutMinutes: Int? = null,
    override val _customArguments: Map<String, CustomValue>,
) : HasCustomArguments {
    private var job = Job(
        id = id,
        name = name,
        runsOn = runsOn,
        needs = needs,
        condition = condition,
        env = env,
        steps = emptyList(),
        strategyMatrix = strategyMatrix,
        timeoutMinutes = timeoutMinutes,
        _customArguments = _customArguments
    )

    fun run(
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): CommandStep = run(
        name = null,
        command = command,
        env = env,
        condition = condition,
    )

    fun run(
        name: String? = null,
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): CommandStep {
        val newStep = CommandStep(
            id = "step-${job.steps.size}",
            name = name,
            command = command,
            env = env,
            condition = condition,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun uses(
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): ExternalActionStep = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
    )

    fun uses(
        name: String? = null,
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): ExternalActionStep {
        val newStep = ExternalActionStep(
            id = "step-${job.steps.size}",
            name = name,
            action = action,
            env = env,
            condition = condition,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun <T> uses(
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): ExternalActionStepWithOutputs<T> = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
    )

    fun <T> uses(
        name: String? = null,
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
    ): ExternalActionStepWithOutputs<T> {
        val stepId = "step-${job.steps.size}"
        val newStep = ExternalActionStepWithOutputs(
            id = stepId,
            name = name,
            action = action,
            env = env,
            condition = condition,
            outputs = action.buildOutputObject(stepId),
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun build() = job
}
