package io.github.typesafegithub.workflows.actionbindinggenerator

public data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
    val deprecatedByVersion: String? = null,
)

/**
 * A top-level action is an action with its `action.y(a)ml` file in the repository root, as opposed to actions stored
 * in subdirectories.
 */
public val ActionCoords.isTopLevel: Boolean get() = "/" !in name

public val ActionCoords.prettyPrint: String get() = "$owner/$name@$version"
