package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.YamlActionArguments
import kotlinx.serialization.Serializable

@Serializable
sealed class YamlStep()

@Serializable
data class YamlRunStep(
    val name: String,
    val run: String,
) : YamlStep()

@Serializable
data class YamlExternalAction(
    val uses: String,
    val with: YamlActionArguments,
) : YamlStep()
