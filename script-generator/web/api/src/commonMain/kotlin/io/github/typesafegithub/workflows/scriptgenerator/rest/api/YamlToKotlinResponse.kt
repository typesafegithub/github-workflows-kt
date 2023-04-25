package io.github.typesafegithub.workflows.scriptgenerator.rest.api

import kotlinx.serialization.Serializable

@Serializable
data class YamlToKotlinResponse(
    val kotlinScript: String? = null,
    val error: String? = null,
)
