package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
sealed class YamlStep

@Serializable
data class YamlRunStep(
    val name: String,
    val run: String,
) : YamlStep()
