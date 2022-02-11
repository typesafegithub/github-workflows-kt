package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest

val wrappersToGenerate = listOf(
    WrapperRequest(ActionCoords("actions", "download-artifact", "v2")),

    WrapperRequest(ActionCoords("madhead", "check-gradle-version", "v1")),
    WrapperRequest(ActionCoords("madhead", "semver-utils", "latest")),

    WrapperRequest(ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")),
)
