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

    WrapperRequest(
        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),
        mapOf(
            "reviewers" to ListOfStringsTyping(","),
            "team-reviewers" to ListOfStringsTyping(","),
            "labels" to ListOfStringsTyping(","),
            "set-distribution-checksum" to BooleanTyping,
            "paths" to ListOfStringsTyping(","),
            "paths-ignore" to ListOfStringsTyping(","),
        ),
    ),

    WrapperRequest(ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")),

    WrapperRequest(
        ActionCoords("repo-sync", "pull-request", "v2"),
        mapOf(
            "pr_reviewer" to ListOfStringsTyping(","),
            "pr_assignee" to ListOfStringsTyping(","),
            "pr_label" to ListOfStringsTyping(","),
            "pr_draft" to BooleanTyping,
            "pr_allow_empty" to BooleanTyping,
        ),
    )
)
