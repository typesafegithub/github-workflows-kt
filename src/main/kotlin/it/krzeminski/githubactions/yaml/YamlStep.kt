package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
data class YamlRunStep(
    val name: String,
    val run: String,
)
