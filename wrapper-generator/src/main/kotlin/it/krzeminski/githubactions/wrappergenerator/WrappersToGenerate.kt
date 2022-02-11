package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords

val wrappersToGenerate = listOf(
    ActionCoords("actions", "download-artifact", "v2"),

    ActionCoords("madhead", "check-gradle-version", "v1"),
    ActionCoords("madhead", "semver-utils", "latest"),

    ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0"),
)
