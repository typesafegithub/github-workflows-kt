package it.krzeminski.githubactions.actions

import kotlinx.serialization.Serializable

data class UploadArtifact(
    val artifactName: String,
    val path: List<String>,
) : Action(name = "actions/upload-artifact@v2")

@Serializable
data class YamlUploadArtifactArguments(
    val name: String,
    val path: String,
) : YamlActionArguments()
