package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.ktor.http.Parameters

fun Parameters.extractActionCoords(
    extractVersion: Boolean,
    owner: String = this["owner"]!!,
): ActionCoords {
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
    val nameAndPathParts = nameAndPath.split("__")
    val name = nameAndPathParts.first()
    val path =
        nameAndPathParts
            .drop(1)
            .joinToString("/")
            .takeUnless { it.isBlank() }
    val version = if (extractVersion) this["version"]!! else "irrelevant"
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

    return ActionCoords(ownerPlain, name, version, significantVersion, path, typesUuid)
}
