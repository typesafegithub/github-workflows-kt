package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
data class YamlWorkflow(
    val name: String,
    val on: YamlTriggers,
    val jobs: Map<String, YamlJob>,
)
