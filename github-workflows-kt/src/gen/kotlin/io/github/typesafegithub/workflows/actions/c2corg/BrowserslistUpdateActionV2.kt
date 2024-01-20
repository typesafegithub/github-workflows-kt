// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.c2corg

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
 *
 * @param githubToken GitHub secret
 * @param branch The pull request branch name
 * @param baseBranch The target branch into which the pull request will be merged
 * @param directory For monorepos, directory to switch to
 * @param commitMessage The message to use when committing changes
 * @param title The title of the pull request
 * @param body The body of the pull request
 * @param labels Labels to associate to the pull request
 * @param reviewers Users to associate to the pull request reviewers list
 * @param teams Teams to associate to the pull request reviewers list
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class BrowserslistUpdateActionV2 private constructor(
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
     * Users to associate to the pull request reviewers list
     */
    public val reviewers: List<String>? = null,
    /**
     * Teams to associate to the pull request reviewers list
     */
    public val teams: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<BrowserslistUpdateActionV2.Outputs>("c2corg", "browserslist-update-action",
        _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        githubToken: String,
        branch: String? = null,
        baseBranch: String? = null,
        directory: String? = null,
        commitMessage: String? = null,
        title: String? = null,
        body: String? = null,
        labels: List<String>? = null,
        reviewers: List<String>? = null,
        teams: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(githubToken=githubToken, branch=branch, baseBranch=baseBranch, directory=directory,
            commitMessage=commitMessage, title=title, body=body, labels=labels, reviewers=reviewers,
            teams=teams, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "github_token" to githubToken,
            branch?.let { "branch" to it },
            baseBranch?.let { "base_branch" to it },
            directory?.let { "directory" to it },
            commitMessage?.let { "commit_message" to it },
            title?.let { "title" to it },
            body?.let { "body" to it },
            labels?.let { "labels" to it.joinToString(",") },
            reviewers?.let { "reviewers" to it.joinToString(",") },
            teams?.let { "teams" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
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
    }
}
