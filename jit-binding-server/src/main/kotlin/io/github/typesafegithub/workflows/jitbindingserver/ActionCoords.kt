package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.ktor.http.Parameters

fun Parameters.extractActionCoords(extractVersion: Boolean): ActionCoords {
    val owner = this["owner"]!!
    val nameAndPathAndSignificantVersionParts = this["name"]!!.split("___", limit = 2)
    val nameAndPath = nameAndPathAndSignificantVersionParts.first()
    val significantVersion =
        nameAndPathAndSignificantVersionParts
            .drop(1)
            .takeIf { it.isNotEmpty() }
            ?.single()
            ?.let { significantVersionString ->
                SignificantVersion
                    .entries
                    .find { "$it" == significantVersionString }
            } ?: FULL
    val pinToCommit = nameAndPathAndSignificantVersionParts
        .drop(1)
        .takeIf { it.isNotEmpty() }
        ?.single() == "commit_lenient"
    val nameAndPathParts = nameAndPath.split("__")
    val name = nameAndPathParts.first()
    val path =
        nameAndPathParts
            .drop(1)
            .joinToString("/")
            .takeUnless { it.isBlank() }
    val version = if (extractVersion) {
        val versionPart = this["version"]!!
        if (pinToCommit) {
            versionPart.split("__")[1]
        } else {
            versionPart
        }
    } else "irrelevant"
    val comment = if (extractVersion) this["version"]!!.split("__")[0] else null

    return ActionCoords(
        owner = owner,
        name = name,
        version = version,
        significantVersion = significantVersion,
        path = path,
        comment = comment,
    )
}
