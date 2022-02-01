package it.krzeminski.githubactions.actions.reposync

import it.krzeminski.githubactions.actions.Action

/**
 * A GitHub Action for creating pull requests.
 * https://github.com/repo-sync/pull-request
 */
data class PullRequestV2(
    /**
     * Branch name to pull from, default is triggered branch
     */
    val sourceBranch: String? = null,
    /**
     * Branch name to sync to in this repo, default is master
     */
    val destinationBranch: String? = null,
    /**
     * Pull request title
     */
    val prTitle: String? = null,
    /**
     * Pull request body
     */
    val prBody: String? = null,
    /**
     * Pull request template
     */
    val prTemplate: String? = null,
    /**
     * Pull request reviewers, comma-separated list (no spaces)
     */
    val prReviewer: String? = null,
    /**
     * Pull request assignees, comma-separated list (no spaces)
     */
    val prAssignee: String? = null,
    /**
     * Pull request labels, comma-separated list (no spaces)
     */
    val prLabel: String? = null,
    /**
     * Pull request milestone
     */
    val prMilestone: String? = null,
    /**
     * Draft pull request
     */
    val prDraft: Boolean? = null,
    /**
     *  Create PR even if no changes
     */
    val prAllowEmpty: Boolean? = null,
    /**
     * GitHub token secret
     */
    val githubToken: String? = null,
) : Action("repo-sync", "pull-request", "v2") {

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            sourceBranch?.let { "source_branch" to it },
            destinationBranch?.let { "destination_branch" to it },
            prTitle?.let { "pr_title" to it },
            prBody?.let { "pr_body" to it },
            prTemplate?.let { "pr_template" to it },
            prReviewer?.let { "pr_reviewer" to it },
            prAssignee?.let { "pr_assignee" to it },
            prLabel?.let { "pr_label" to it },
            prMilestone?.let { "pr_milestone" to it },
            prDraft?.let { "pr_draft" to "$it" },
            prAllowEmpty?.let { "pr_allow_empty" to "$it" },
            githubToken?.let { "github_token" to it },
        ).toTypedArray()
    )
}
