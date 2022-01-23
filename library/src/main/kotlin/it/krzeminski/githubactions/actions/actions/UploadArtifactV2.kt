package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class UploadArtifactV2(
    val artifactName: String,
    val path: List<String>,
) : Action(name = "actions/upload-artifact@v2") {
    override fun toYamlArguments() = linkedMapOf(
        "name" to artifactName,
        "path" to path.joinToString(separator = "\n"),
    )
}
