package it.krzeminski.githubactions.scriptmodel

import kotlinx.serialization.Serializable

@Serializable
data class YamlWorkflow(
    val name: String,
    val on: YamlWorkflowTriggers,
    val jobs: Map<String, YamlJob> = emptyMap(),
    val env: Map<String, String> = emptyMap(),
) {
    override fun toString() = """
       name: $name
       on:
         $on
       jobs:
         ${jobs.entries.joinToString(separator = "\n         ")}
    """.trimIndent()
}
