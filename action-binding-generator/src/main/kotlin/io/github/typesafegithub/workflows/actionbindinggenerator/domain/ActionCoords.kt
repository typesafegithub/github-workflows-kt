package io.github.typesafegithub.workflows.actionbindinggenerator.domain

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL

public data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
    val versionForExtractingTypings: String = version,
    val rawName: String,
    val rawVersion: String? = null,
    /**
     * The version part that is significant when generating the YAML output,
     * i.e. whether to write the full version, only the major version or major and minor version.
     * This is used to enable usage of Maven ranges without needing to specify a custom version
     * each time instantiating an action.
     * The value of this property is part of the Maven coordinates as a suffix for the [name] property.
     */
    val significantVersion: SignificantVersion = FULL,
    val path: String? = null,
    val comment: String? = null,
)

/**
 * A top-level action is an action with its `action.y(a)ml` file in the repository root, as opposed to actions stored
 * in subdirectories.
 */
public val ActionCoords.isTopLevel: Boolean get() = path == null

public val ActionCoords.prettyPrint: String get() = "$prettyPrintWithoutVersion@$version"

public val ActionCoords.prettyPrintWithoutVersion: String get() = "$owner/$fullName${
    significantVersion.takeUnless { it == FULL }?.let { " with $it version" } ?: ""
}"

/**
 * For most actions, it's empty.
 * For actions that aren't executed from the root of the repo, it returns the path relative to the repo root where the
 * action lives, starting with a slash.
 */
public val ActionCoords.subName: String get() = path?.let { "/$path" } ?: ""

/**
 * For most actions, it's equal to [ActionCoords.name].
 * For actions that aren't executed from the root of the repo, it returns the path starting with the repo root where the
 * action lives.
 */
public val ActionCoords.fullName: String get() = "$name$subName"
