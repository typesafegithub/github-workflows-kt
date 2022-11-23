package it.krzeminski.githubactions.wrappergenerator.domain

data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
    val deprecatedByVersion: String? = null,
)
