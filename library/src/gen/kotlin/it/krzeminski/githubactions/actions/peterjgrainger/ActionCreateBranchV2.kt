// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.peterjgrainger

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Create Branch
 *
 * Creates a branch
 *
 * [Action on GitHub](https://github.com/peterjgrainger/action-create-branch)
 */
public class ActionCreateBranchV2(
    /**
     * The branch to create
     */
    public val branch: String? = null,
    /**
     * The SHA1 value for the branch reference
     */
    public val sha: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("peterjgrainger", "action-create-branch", _customVersion ?: "v2.1.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            branch?.let { "branch" to it },
            sha?.let { "sha" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
