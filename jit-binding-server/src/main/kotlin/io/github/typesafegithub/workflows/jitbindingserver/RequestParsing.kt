package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.ktor.http.Parameters

/**
 * Returns `null` if the request doesn't make sense and the service should return no resource.
 */
fun Parameters.parseRequest(
    extractVersion: Boolean,
    owner: String = this["owner"]!!,
): BindingsServerRequest? {
    val nameAndPathAndSignificantVersionParts = this["name"]!!.split("___", limit = 2)
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
    val nameAndPath = if (significantVersion == FULL) this["name"]!! else nameAndPathAndSignificantVersionParts.first()
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
            if (significantVersion == COMMIT_LENIENT) {
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
    val comment =
        if ((significantVersion == COMMIT_LENIENT) && extractVersion) this["version"]!!.split("__")[0] else null
    val versionForTypings = if (extractVersion) this["version"]!!.split("__")[0] else "irrelevant"
    // we cannot give the types UUID separately from the post handler
    // only in the post handler we generate the UUID, but for the other
    // handlers the UUID part is already coming through the request as part of the owner
    val ownerAndTypesUuid = owner.split("__types__", limit = 2)
    val ownerPlain = ownerAndTypesUuid.first()
    val typesUuid =
        ownerAndTypesUuid
            .drop(1)
            .takeIf { it.isNotEmpty() }
            ?.single()

    return BindingsServerRequest(
        rawOwner = owner,
        rawName = this["name"]!!,
        rawVersion = this["version"],
        actionCoords =
            ActionCoords(
                owner = ownerPlain,
                name = name,
                version = version,
                versionForTypings = versionForTypings,
                significantVersion = significantVersion,
                path = path,
                comment = comment,
                typesUuid = typesUuid,
            ),
    )
}
