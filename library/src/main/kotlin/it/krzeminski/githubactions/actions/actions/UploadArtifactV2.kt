package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

class UploadArtifactV2(
    val name: String,
    val path: List<String>,
) : Action("actions", "upload-artifact", "v2") {
    override fun toYamlArguments() = linkedMapOf(
        "name" to name,
        "path" to path.joinToString(separator = "\n"),
    )
}
