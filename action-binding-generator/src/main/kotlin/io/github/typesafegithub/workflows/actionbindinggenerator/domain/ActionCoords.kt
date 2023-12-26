package io.github.typesafegithub.workflows.actionbindinggenerator.domain

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

/**
 * For most actions, it's the same as [ActionCoords.name].
 * For actions that aren't executed from the root of the repo, it returns the repo name.
 */
public val ActionCoords.repoName: String get() =
    name.substringBefore("/")

/**
 * For most actions, it's empty.
 * For actions that aren't executed from the root of the repo, it returns the path relative to the repo root where the
 * action lives.
 */
public val ActionCoords.subName: String get() =
    if (isTopLevel) "" else name.substringAfter("/")

internal fun String.toActionCoords(): ActionCoords {
    val (ownerAndName, version) = this.split('@')
    val (owner, name) = ownerAndName.split('/', limit = 2)
    return ActionCoords(
        owner = owner,
        name = name,
        version = version,
    )
}
