package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.ktor.http.Parameters

fun Parameters.extractActionCoords(
    extractVersion: Boolean,
    owner: String = this["owner"]!!,
): ActionCoords {
    val nameAndPath = this["name"]!!.split("__")
    val name = nameAndPath.first()
    val path = nameAndPath.drop(1).joinToString("/").takeUnless { it.isBlank() }
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

    return ActionCoords(ownerPlain, name, version, path, typesUuid)
}
