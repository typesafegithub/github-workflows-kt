package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.ktor.http.Parameters

/**
 * Returns `null` if the request doesn't make sense and the service should return no resource.
 */
fun Parameters.parseRequest(extractVersion: Boolean): BindingsServerRequest? {
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
    val pinToCommit =
        nameAndPathAndSignificantVersionParts
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
    val version =
        if (extractVersion) {
            val versionPart = this["version"]!!
            if (pinToCommit) {
                val versionParts = versionPart.split("__")
                if (versionParts.size < 2) {
                    return null
                }
                versionParts[1]
            } else {
                versionPart
            }
        } else {
            "irrelevant"
        }
    val comment = if (pinToCommit && extractVersion) this["version"]!!.split("__")[0] else null
    val versionForTypings = if (extractVersion) this["version"]!!.split("__")[0] else "irrelevant"

    return BindingsServerRequest(
        rawName = this["name"]!!,
        rawVersion = this["version"],
        actionCoords =
            ActionCoords(
                owner = owner,
                name = name,
                version = version,
                versionForTypings = versionForTypings,
                significantVersion = significantVersion,
                path = path,
                comment = comment,
            ),
    )
}
