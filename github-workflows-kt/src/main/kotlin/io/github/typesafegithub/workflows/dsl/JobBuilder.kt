package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.CommandStep
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.KotlinLogicStep
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Shell
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.contexts.Contexts
import kotlinx.serialization.Contextual
import kotlin.io.path.name

@Suppress("LongParameterList")
@GithubActionsDsl
public class JobBuilder<OUTPUT : JobOutputs>(
    public val id: String,
    public val name: String?,
    public val runsOn: RunnerType,
    public val needs: List<Job<*>>,
    public val env: LinkedHashMap<String, String>,
    public val condition: String?,
    public val strategyMatrix: Map<String, List<String>>?,
    public val permissions: Map<Permission, Mode>? = null,
    public val timeoutMinutes: Int? = null,
    public val concurrency: Concurrency? = null,
    public val container: Container? = null,
    public val environment: Environment? = null,
    public val services: Map<String, Container> = emptyMap(),
    public val jobOutputs: OUTPUT,
    override val _customArguments: Map<String, @Contextual Any?>,
    private val workflowBuilder: WorkflowBuilder,
) : HasCustomArguments {
    private var job =
        Job<OUTPUT>(
            id = id,
            name = name,
            runsOn = runsOn,
            needs = needs,
            condition = condition,
            env = env,
            steps = emptyList(),
            strategyMatrix = strategyMatrix,
            permissions = permissions,
            timeoutMinutes = timeoutMinutes,
            concurrency = concurrency,
            container = container,
            environment = environment,
            services = services,
            outputs = jobOutputs,
            _customArguments = _customArguments,
        )

    public fun run(
        @Suppress("UNUSED_PARAMETER")
        vararg pleaseUseNamedArguments: Unit,
        command: String,
        name: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        @SuppressWarnings("FunctionParameterNaming")
        `if`: String? = null,
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        shell: Shell? = null,
        workingDirectory: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): CommandStep {
        require(!(`if` != null && condition != null)) {
            "Either 'if' or 'condition' have to be set, not both!"
        }

        val newStep =
            CommandStep(
                id = "step-${job.steps.size}",
                name = name,
                command = command,
                env = env,
                condition = `if` ?: condition,
                continueOnError = continueOnError,
                timeoutMinutes = timeoutMinutes,
                shell = shell,
                workingDirectory = workingDirectory,
                _customArguments = _customArguments,
            )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    @ExperimentalKotlinLogicStep
    public fun run(
        @Suppress("UNUSED_PARAMETER")
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        @SuppressWarnings("FunctionParameterNaming")
        `if`: String? = null,
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        shell: Shell? = null,
        workingDirectory: String? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
        logic: Contexts.() -> Unit,
    ): KotlinLogicStep {
        require(!(`if` != null && condition != null)) {
            "Either 'if' or 'condition' have to be set, not both!"
        }
        require(job.steps.filterIsInstance<ActionStep<*>>().any { "/checkout@" in it.action.usesString }) {
            "Please check out the code prior to using Kotlin-based 'run' block!"
        }
        val sourceFile =
            workflowBuilder.workflow.sourceFile
                ?: throw IllegalArgumentException("sourceFile needs to be set when using Kotlin-based 'run' block!")
        val id = "step-${job.steps.size}"

        val newStep =
            KotlinLogicStep(
                id = id,
                name = name,
                // Because of the current architecture, it's hard to make this command work properly if the sourceFile
                // isn't in .github/workflows directory. It's the most common use case, though, so for now this
                // simplified implementation is used.
                command = "GHWKT_RUN_STEP='${this.id}:$id' '.github/workflows/${sourceFile.name}'",
                logic = logic,
                env = LinkedHashMap(env.toMap() + mapOf("GHWKT_GITHUB_CONTEXT_JSON" to "${'$'}{{ toJSON(github) }}")),
                condition = `if` ?: condition,
                continueOnError = continueOnError,
                timeoutMinutes = timeoutMinutes,
                shell = shell,
                workingDirectory = workingDirectory,
                _customArguments = _customArguments,
            )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    public fun <T : Action.Outputs> uses(
        @Suppress("UNUSED_PARAMETER")
        vararg pleaseUseNamedArguments: Unit,
        action: Action<T>,
        name: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        @SuppressWarnings("FunctionParameterNaming")
        `if`: String? = null,
        condition: String? = null,
        continueOnError: Boolean? = null,
        timeoutMinutes: Int? = null,
        @SuppressWarnings("FunctionParameterNaming")
        _customArguments: Map<String, @Contextual Any> = mapOf(),
    ): ActionStep<T> {
        val stepId = "step-${job.steps.size}"
        require(!(`if` != null && condition != null)) {
            "Either 'if' or 'condition' have to be set, not both!"
        }
        val newStep =
            ActionStep(
                id = stepId,
                name = name,
                action = action,
                env = env,
                condition = `if` ?: condition,
                continueOnError = continueOnError,
                timeoutMinutes = timeoutMinutes,
                outputs = action.buildOutputObject(stepId),
                _customArguments = _customArguments,
            )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    public fun build(): Job<OUTPUT> = job
}
