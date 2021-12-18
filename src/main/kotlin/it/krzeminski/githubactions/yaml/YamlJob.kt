package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
data class YamlJob(
    val name: String,
)
