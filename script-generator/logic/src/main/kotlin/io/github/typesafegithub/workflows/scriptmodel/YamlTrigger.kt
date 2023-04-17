package io.github.typesafegithub.workflows.scriptmodel

import kotlinx.serialization.Serializable

/**
 * Used for triggers that don't have yet a more specific type-safe wrapper
 */
@Serializable
data class YamlTrigger(
    val types: List<String>? = null,
)

@Serializable
data class ScheduleValue(
    val cron: String,
)
