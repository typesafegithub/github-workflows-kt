package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.FreeYamlArgs
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs

data class Job(
    val name: String,
    val runsOn: RunnerType,
    val steps: List<Step>,
    val needs: List<Job> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
) : HasFreeYamlArgs {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()
}
