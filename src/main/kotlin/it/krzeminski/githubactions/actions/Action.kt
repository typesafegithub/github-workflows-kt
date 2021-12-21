package it.krzeminski.githubactions.actions

import kotlinx.serialization.Serializable

sealed class Action(
    val name: String,
)

@Serializable
sealed class YamlActionArguments
