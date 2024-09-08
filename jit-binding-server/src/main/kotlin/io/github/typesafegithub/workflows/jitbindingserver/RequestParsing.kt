package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.ktor.http.Parameters
import kotlin.text.split

/**
 * Returns `null` if the request doesn't make sense and the service should return no resource.
 */
fun Parameters.parseRequest(extractVersion: Boolean): BindingsServerRequest? {
    val owner = this["owner"]!!
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
    val parsedVersion = parseVersion(extractVersion, significantVersion)

    return if ((parsedVersion.version == null) || (parsedVersion.bindingVersion == null)) {
        null
    } else {
        BindingsServerRequest(
            rawName = this["name"]!!,
            rawVersion = this["version"],
            bindingVersion = parsedVersion.bindingVersion,
            actionCoords =
                ActionCoords(
                    owner = owner,
                    name = name,
                    version = parsedVersion.version,
                    versionForTypings = parsedVersion.versionForTypings!!,
                    significantVersion = significantVersion,
                    path = path,
                    comment = parsedVersion.comment,
                ),
        )
    }
}

private fun Parameters.parseVersion(
    extractVersion: Boolean,
    significantVersion: SignificantVersion,
): ParsedVersion =
    if (extractVersion) {
        val bindingVersionAndVersionParts = this["version"]!!.split("___", limit = 2)
        val (bindingVersion, versionPart) =
            when {
                bindingVersionAndVersionParts.size == 1 -> {
                    V1 to bindingVersionAndVersionParts[0]
                }

                !bindingVersionAndVersionParts[0].startsWith("binding_version_") -> {
                    null to bindingVersionAndVersionParts[1]
                }

                else -> {
                    val bindingVersionPart = bindingVersionAndVersionParts[0].substringAfter("binding_version_")
                    val bindingVersion =
                        BindingVersion
                            .entries
                            .find {
                                it.name.lowercase() == bindingVersionPart
                            }
                    if ((bindingVersion?.isExperimental == true) &&
                        !bindingVersionAndVersionParts[1].endsWith("-beta")
                    ) {
                        null to bindingVersionAndVersionParts[1]
                    } else if (bindingVersion?.isExperimental == true) {
                        bindingVersion to bindingVersionAndVersionParts[1].removeSuffix("-beta")
                    } else {
                        bindingVersion to bindingVersionAndVersionParts[1]
                    }
                }
            }
        val (version, comment) =
            if (significantVersion == COMMIT_LENIENT) {
                val versionParts = versionPart.split("__", limit = 2)
                if (versionParts.size == 1) {
                    null to null
                } else {
                    versionParts[1] to versionParts[0]
                }
            } else {
                versionPart to null
            }
        ParsedVersion(
            bindingVersion = bindingVersion,
            version = version,
            comment = comment,
            versionForTypings = comment ?: version,
        )
    } else {
        ParsedVersion(
            bindingVersion = V1,
            version = "irrelevant",
            comment = null,
            versionForTypings = "irrelevant",
        )
    }

private data class ParsedVersion(
    val bindingVersion: BindingVersion?,
    val version: String?,
    val comment: String?,
    val versionForTypings: String?,
)
