package it.krzeminski.githubactions.actions

import kotlinx.serialization.Serializable

sealed class Action(
    val name: String,
) {
    abstract fun toYamlArguments(): YamlActionArguments
}

@Serializable
sealed class YamlActionArguments
