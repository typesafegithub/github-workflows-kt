// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.c2corg

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: browserslist-update-action
 *
 * Runs `npx update-browserslist-db@latest` on a repository and proposes a pull request to merge
 * updates.
 *
 * [Action on GitHub](https://github.com/c2corg/browserslist-update-action)
 */
public data class BrowserslistUpdateActionV2(
    /**
     * GitHub secret
     */
    public val githubToken: String,
    /**
     * The pull request branch name
     */
    public val branch: String? = null,
    /**
     * The target branch into which the pull request will be merged
     */
    public val baseBranch: String? = null,
    /**
     * For monorepos, directory to switch to
     */
    public val directory: String? = null,
    /**
     * The message to use when committing changes
     */
    public val commitMessage: String? = null,
    /**
     * The title of the pull request
     */
    public val title: String? = null,
    /**
     * The body of the pull request
     */
    public val body: String? = null,
    /**
     * Labels to associate to the pull request
     */
    public val labels: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<BrowserslistUpdateActionV2.Outputs>("c2corg", "browserslist-update-action",
        _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "github_token" to githubToken,
            branch?.let { "branch" to it },
            baseBranch?.let { "base_branch" to it },
            directory?.let { "directory" to it },
            commitMessage?.let { "commit_message" to it },
            title?.let { "title" to it },
            body?.let { "body" to it },
            labels?.let { "labels" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * A boolean set to true when changes were found and a pull request was created or updated.
         */
        public val hasPr: String = "steps.$stepId.outputs.has_pr"

        /**
         * The pull request number, if applies.
         */
        public val prNumber: String = "steps.$stepId.outputs.pr_number"

        /**
         * Whether the pull request was created or updated, if applies.
         */
        public val prStatus: String = "steps.$stepId.outputs.pr_status"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
