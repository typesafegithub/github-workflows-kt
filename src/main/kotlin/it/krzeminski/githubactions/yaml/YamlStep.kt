package it.krzeminski.githubactions.yaml

import kotlinx.serialization.Serializable

@Serializable
sealed class YamlStep

@Serializable
data class YamlRunStep(
    val run: String,
) : YamlStep()
