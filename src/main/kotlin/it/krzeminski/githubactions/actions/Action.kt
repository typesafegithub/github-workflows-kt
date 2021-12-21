package it.krzeminski.githubactions.actions

import kotlinx.serialization.Serializable

abstract class Action(
    val name: String,
)

@Serializable
sealed class YamlActionArguments
