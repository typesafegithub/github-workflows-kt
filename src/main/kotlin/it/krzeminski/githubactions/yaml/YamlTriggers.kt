package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
data class YamlTriggers(
    val workflow_dispatch: Map<String, String>?,
)
