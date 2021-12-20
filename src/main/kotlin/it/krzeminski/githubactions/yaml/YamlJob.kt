package it.krzeminski.githubactions.yaml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YamlJob(
    @SerialName("runs-on")
    val runsOn: String,
    val steps: List<YamlRunStep>,
)
