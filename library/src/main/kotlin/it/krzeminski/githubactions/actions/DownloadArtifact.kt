package it.krzeminski.githubactions.actions

import kotlinx.serialization.Serializable

data class DownloadArtifact(
    val artifactName: String,
    val path: String,
) : Action(name = "actions/download-artifact@v2") {
    override fun toYamlArguments() = YamlDownloadArtifactArguments(
        name = artifactName,
        path = path,
    )
}

@Serializable
data class YamlDownloadArtifactArguments(
    val name: String,
    val path: String,
) : YamlActionArguments()
