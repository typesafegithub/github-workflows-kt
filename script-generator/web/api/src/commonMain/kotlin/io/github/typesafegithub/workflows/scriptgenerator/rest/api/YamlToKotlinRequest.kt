package io.github.typesafegithub.workflows.scriptgenerator.rest.api

import kotlinx.serialization.Serializable

@Serializable
data class YamlToKotlinRequest(
    val yaml: String,
)
