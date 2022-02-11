package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfStringsTyping

val wrappersToGenerate = listOf(
    WrapperRequest(ActionCoords("actions", "download-artifact", "v2")),

    WrapperRequest(ActionCoords("madhead", "check-gradle-version", "v1")),
    WrapperRequest(ActionCoords("madhead", "read-java-properties", "latest"), mapOf("all" to BooleanTyping)),
    WrapperRequest(ActionCoords("madhead", "semver-utils", "latest")),

    WrapperRequest(
        ActionCoords("gradle", "gradle-build-action", "v2"),
        mapOf(
            "cache-disabled" to BooleanTyping,
            "cache-read-only" to BooleanTyping,
            "gradle-home-cache-includes" to ListOfStringsTyping("\\n"),
            "gradle-home-cache-excludes" to ListOfStringsTyping("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("gradle", "wrapper-validation-action", "v1"),
        mapOf(
            "min-wrapper-count" to IntegerTyping,
            "allow-snapshots" to BooleanTyping,
            "allow-checksums" to ListOfStringsTyping(","),
        ),
    ),

    WrapperRequest(ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")),
)
