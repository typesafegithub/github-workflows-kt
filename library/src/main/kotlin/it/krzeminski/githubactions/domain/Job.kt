package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.validation.requireMatchesRegex
import kotlinx.serialization.Contextual

data class Job<OUTPUT : JobOutputs>(
    val id: String,
    val name: String? = null,
    val runsOn: RunnerType,
    val steps: List<Step>,
    val jobOutputs: OUTPUT,
    val needs: List<Job<*>> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
    val timeoutMinutes: Int? = null,
    val concurrency: Concurrency? = null,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : HasCustomArguments {
    companion object {
        @Suppress("LongParameterList")
        operator fun invoke(
            id: String,
            name: String? = null,
            runsOn: RunnerType,
            steps: List<Step>,
            needs: List<Job<*>> = emptyList(),
            env: LinkedHashMap<String, String> = linkedMapOf(),
            condition: String? = null,
            strategyMatrix: Map<String, List<String>>? = null,
            timeoutMinutes: Int? = null,
            concurrency: Concurrency? = null,
            @Suppress("FunctionParameterNaming")
            _customArguments: Map<String, @Contextual Any> = mapOf(),
        ) = Job(
            id = id,
            name = name,
            runsOn = runsOn,
            steps = steps,
            jobOutputs = JobOutputs.EMPTY,
            needs = needs,
            env = env,
            condition = condition,
            strategyMatrix = strategyMatrix,
            timeoutMinutes = timeoutMinutes,
            concurrency = concurrency,
            _customArguments = _customArguments
        )
    }

    init {
        requireMatchesRegex(
            field = "Job.id",
            value = id,
            regex = Regex("[a-zA-Z_][a-zA-Z0-9_-]*"),
            url = "https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job"
        )
        timeoutMinutes?.let { value ->
            require(value > 0) { "timeout should be positive" }
        }
        jobOutputs.job = this
    }
}
