package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments

data class Job(
    val name: String,
    val runsOn: RunnerType,
    val steps: List<Step>,
    val needs: List<Job> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : HasCustomArguments
