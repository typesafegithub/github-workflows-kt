package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class DownloadArtifactV2(
    val name: String,
    val path: String,
) : Action("actions", "download-artifact", "v2") {
    override fun toYamlArguments() = linkedMapOf(
        "name" to name,
        "path" to path,
    )
}
