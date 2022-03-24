package it.krzeminski.githubactions.scriptmodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YamlJob(
    val name: String? = null,
    @SerialName("runs-on")
    val runsOn: String,
    val steps: List<YamlStep>,
    val needs: List<String> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
)
