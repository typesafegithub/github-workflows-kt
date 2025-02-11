package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.ktor.http.Parameters

fun Parameters.extractActionCoords(extractVersion: Boolean): ActionCoords {
    val owner = this["owner"]!!
    val nameAndPath = this["name"]!!.split("__")
    val name = nameAndPath.first()
    val path = nameAndPath.drop(1).joinToString("/").takeUnless { it.isBlank() }
    val version = if (extractVersion) this["version"]!! else "irrelevant"

    return ActionCoords(owner, name, version, path)
}
