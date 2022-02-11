// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.peterjgrainger

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: Create Branch
 *
 * Creates a branch
 *
 * https://github.com/peterjgrainger/action-create-branch
 */
public class ActionCreateBranchV2(
    /**
     * The branch to create
     */
    public val branch: String? = null,
    /**
     * The SHA1 value for the branch reference
     */
    public val sha: String? = null
) : Action("peterjgrainger", "action-create-branch", "v2.1.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            branch?.let { "branch" to it },
            sha?.let { "sha" to it },
        ).toTypedArray()
    )
}
