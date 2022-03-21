package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.validation.requireMatchesRegex

data class Job(
    val name: String,
    val runsOn: RunnerType,
    val steps: List<Step>,
    val needs: List<Job> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
    val timeoutMinutes: Int? = null,
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : HasCustomArguments {
    init {
        requireMatchesRegex(
            field = "Job.name",
            value = name,
            regex = Regex("[a-zA-Z_][a-zA-Z0-9_-]*"),
            url = "https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job"
        )
        timeoutMinutes?.let { value ->
            require(value > 0) { """timeout should be positive""" }
        }
    }
}
