@file:Suppress("RedundantVisibilityModifier")

package it.krzeminski.githubactions.actions.peterjgrainger

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action Create Branch
 *
 * Creates a branch
 *
 * http://github.com/peterjgrainger/action-create-branch
 */
public data class ActionCreateBranchV2(
    /**
     * The branch to create
     */
    public val branch: String? = null,
    /**
     * The SHA1 value for the branch reference
     */
    public val sha: String? = null
) : Action("peterjgrainger", "action-create-branch", "v2.1.0") {
    public override fun toYamlArguments(): LinkedHashMap<String, String> = yamlOf(
        "branch" to branch,
        "sha" to sha,
    )

    internal companion object {
        public val example_full_action: ActionCreateBranchV2 = ActionCreateBranchV2(
            branch = "release-candidate",
            sha = "sha",
        )

        public val example_full_map: Map<String, String> = mapOf(
            "branch" to "release-candidate",
            "sha" to "sha",
        )
    }
}
