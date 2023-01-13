package it.krzeminski.githubactions.actionsmetadata.model

data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
    val deprecatedByVersion: String? = null,
)

internal val ActionCoords.prettyPrint: String get() = "$owner/$name@$version"
