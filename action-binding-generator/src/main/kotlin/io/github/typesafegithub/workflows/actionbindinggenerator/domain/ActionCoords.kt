package io.github.typesafegithub.workflows.actionbindinggenerator.domain

public data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
    val path: String? = null,
    val typesUuid: String? = null,
)

/**
 * A top-level action is an action with its `action.y(a)ml` file in the repository root, as opposed to actions stored
 * in subdirectories.
 */
public val ActionCoords.isTopLevel: Boolean get() = path == null

public val ActionCoords.prettyPrint: String get() = "$owner/$fullName@$version${typesUuid?.let { " (types: $it)" } ?: ""}"

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

internal fun String.toActionCoords(): ActionCoords {
    val (coordinates, version) = this.split('@')
    val coordinateParts = coordinates.split('/')
    val (owner, name) = coordinateParts
    return ActionCoords(
        owner = owner,
        name = name,
        version = version,
        path = coordinateParts.drop(2).joinToString("/").takeUnless { it.isBlank() },
    )
}
