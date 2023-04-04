package io.github.typesafegithub.workflows.scriptmodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YamlStep(
    val id: String? = null,
    val name: String? = null,
    val run: String? = null,
    val uses: String? = null,
    val with: LinkedHashMap<String, String?> = linkedMapOf(),
    val env: LinkedHashMap<String, String?> = linkedMapOf(),
    @SerialName("if")
    val condition: String? = null,
)
