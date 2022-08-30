package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.ActionWithOutputs
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.ExternalActionStepWithOutputs
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Shell
import it.krzeminski.githubactions.dsl.expressions.expr
import kotlinx.serialization.Contextual

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
    val concurrency: Concurrency? = null,
    val outputsMapping: LinkedHashMap<String,String> = linkedMapOf(),
    override val _customArguments: Map<String, @Contextual Any>,
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
        concurrency = concurrency,
        outputsMapping = outputsMapping,
        _customArguments = _customArguments,
    )

    fun run(
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        shell: Shell? = null,
        workingDirectory: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): CommandStep = run(
        name = null,
        command = command,
        env = env,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        shell = shell,
        workingDirectory = workingDirectory,
        _customArguments = _customArguments,
    )

    fun run(
        name: String? = null,
        command: String,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        shell: Shell? = null,
        workingDirectory: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): CommandStep {
        val newStep = CommandStep(
            id = "step-${job.steps.size}",
            name = name,
            command = command,
            env = env,
            condition = condition,
            continueOnError = continueOnError,
            timeoutMinutes = timeoutMinutes,
            shell = shell,
            workingDirectory = workingDirectory,
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun uses(
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): ExternalActionStep = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        _customArguments = _customArguments,
    )

    fun uses(
        name: String? = null,
        action: Action,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): ExternalActionStep {
        val newStep = ExternalActionStep(
            id = "step-${job.steps.size}",
            name = name,
            action = action,
            env = env,
            condition = condition,
            continueOnError = continueOnError,
            timeoutMinutes = timeoutMinutes,
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun <T> uses(
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): ExternalActionStepWithOutputs<T> = uses(
        name = null,
        action = action,
        env = env,
        condition = condition,
        continueOnError = continueOnError,
        timeoutMinutes = timeoutMinutes,
        _customArguments = _customArguments,
    )

    fun <T> uses(
        name: String? = null,
        action: ActionWithOutputs<T>,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): ExternalActionStepWithOutputs<T> {
        val stepId = "step-${job.steps.size}"
        val newStep = ExternalActionStepWithOutputs(
            id = stepId,
            name = name,
            action = action,
            env = env,
            condition = condition,
            continueOnError = continueOnError,
            timeoutMinutes = timeoutMinutes,
            outputs = action.buildOutputObject(stepId),
            _customArguments = _customArguments,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun outputMapping(
        key: String,
        step: CommandStep,
        output: String,
    ) {
        outputsMapping[key] = expr("steps.${step.id}.outputs.$output")
    }
    fun CommandStep.withOutputMapping(
        key: String,
        output: String,
    ): CommandStep {
        outputsMapping[key] = expr("steps.${id}.outputs.$output")
        return this
    }
    fun <T> outputMapping(
        key: String,
        step: ExternalActionStepWithOutputs<T>,
        block: T.() -> String,
    ) {
        outputsMapping[key] = expr(step.outputs.block())
    }
    fun <O, S: ExternalActionStepWithOutputs<O>> S.withOutputMapping(
        key: String,
        block: O.() -> String,
    ): S {
        outputsMapping[key] = expr(outputs.block())
        return this
    }

    fun build() = job
}
