package it.krzeminski.githubactions.yaml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YamlJob(
    @SerialName("runs-on")
    val runsOn: String,
    val steps: List<YamlStep>,
    val needs: List<String>,
    val strategy: YamlStrategy?,
)

@Serializable
data class YamlStrategy(
    val matrix: Map<String, List<String>>? = null,
)