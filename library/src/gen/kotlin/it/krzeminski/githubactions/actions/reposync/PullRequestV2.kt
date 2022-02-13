// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.reposync

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: GitHub Pull Request Action
 *
 * ⤵️ Create pull request
 *
 * [Action on GitHub](https://github.com/repo-sync/pull-request)
 */
public class PullRequestV2(
    /**
     * Branch name to pull from, default is triggered branch
     */
    public val sourceBranch: String? = null,
    /**
     * Branch name to sync to in this repo, default is master
     */
    public val destinationBranch: String? = null,
    /**
     * Pull request title
     */
    public val prTitle: String? = null,
    /**
     * Pull request body
     */
    public val prBody: String? = null,
    /**
     * Pull request template
     */
    public val prTemplate: String? = null,
    /**
     * Pull request reviewers, comma-separated list (no spaces)
     */
    public val prReviewer: List<String>? = null,
    /**
     * Pull request assignees, comma-separated list (no spaces)
     */
    public val prAssignee: List<String>? = null,
    /**
     * Pull request labels, comma-separated list (no spaces)
     */
    public val prLabel: List<String>? = null,
    /**
     * Pull request milestone
     */
    public val prMilestone: String? = null,
    /**
     * Draft pull request
     */
    public val prDraft: Boolean? = null,
    /**
     * Create PR even if no changes
     */
    public val prAllowEmpty: Boolean? = null,
    /**
     * GitHub token secret
     */
    public val githubToken: String
) : Action("repo-sync", "pull-request", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            sourceBranch?.let { "source_branch" to it },
            destinationBranch?.let { "destination_branch" to it },
            prTitle?.let { "pr_title" to it },
            prBody?.let { "pr_body" to it },
            prTemplate?.let { "pr_template" to it },
            prReviewer?.let { "pr_reviewer" to it.joinToString(",") },
            prAssignee?.let { "pr_assignee" to it.joinToString(",") },
            prLabel?.let { "pr_label" to it.joinToString(",") },
            prMilestone?.let { "pr_milestone" to it },
            prDraft?.let { "pr_draft" to it.toString() },
            prAllowEmpty?.let { "pr_allow_empty" to it.toString() },
            "github_token" to githubToken,
        ).toTypedArray()
    )
}
