package it.krzeminski.githubactions.actions

data class DownloadArtifactV2(
    val artifactName: String,
    val path: String,
) : Action(name = "actions/download-artifact@v2") {
    override fun toYamlArguments() = linkedMapOf(
        "name" to artifactName,
        "path" to path,
    )
}
