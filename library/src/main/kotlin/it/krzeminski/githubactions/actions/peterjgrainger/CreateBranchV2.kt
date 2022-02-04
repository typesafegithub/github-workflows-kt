package it.krzeminski.githubactions.actions.peterjgrainger

import it.krzeminski.githubactions.actions.Action

/**
 * This action creates a new branch with the same commit reference
 * as the branch it is being ran on, or your chosen reference when specified.
 * https://github.com/peterjgrainger/action-create-branch
 */
data class CreateBranchV2(
    /** The name of the branch to create. Default "release-candidate". **/
    val branch: String? = null,
    /** The SHA1 value for the branch reference. **/
    val sha: String? = null,
) : Action("peterjgrainger", "action-create-branch", "v2") {

    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            branch?.let { "branch" to it },
            sha?.let { "sha" to it }
        ).toTypedArray()
    )
}
