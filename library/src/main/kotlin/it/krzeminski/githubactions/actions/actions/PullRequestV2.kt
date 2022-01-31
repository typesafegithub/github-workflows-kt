package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

/**
 * A GitHub Action for creating pull requests.
 * https://github.com/repo-sync/pull-request
 */
@Suppress("ConstructorParameterNaming")
data class PullRequestV2(
    /**
     * Branch name to pull from, default is triggered branch
     */
    val source_branch: String? = null,
    /**
     * Branch name to sync to in this repo, default is master
     */
    val destination_branch: String? = null,
    /**
     * Pull request title
     */
    val pr_title: String? = null,
    /**
     * Pull request body
     */
    val pr_body: String? = null,
    /**
     * Pull request template
     */
    val pr_template: String? = null,
    /**
     * Pull request reviewers, comma-separated list (no spaces)
     */
    val pr_reviewer: String? = null,
    /**
     * Pull request assignees, comma-separated list (no spaces)
     */
    val pr_assignee: String? = null,
    /**
     * Pull request labels, comma-separated list (no spaces)
     */
    val pr_label: String? = null,
    /**
     * Pull request milestone
     */
    val pr_milestone: String? = null,
    /**
     * Draft pull request
     */
    val pr_draft: Boolean? = null,
    /**
     *  Create PR even if no changes
     */
    val pr_allow_empty: Boolean? = null,
    /**
     * GitHub token secret
     */
    val github_token: String? = null,
) : Action("repo-sync", "pull-request", "v2.6.1") {

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            source_branch?.let { "source_branch" to it },
            destination_branch?.let { "destination_branch" to it },
            pr_title?.let { "pr_title" to it },
            pr_body?.let { "pr_body" to it },
            pr_template?.let { "pr_template" to it },
            pr_reviewer?.let { "pr_reviewer" to it },
            pr_assignee?.let { "pr_assignee" to it },
            pr_label?.let { "pr_label" to it },
            pr_milestone?.let { "pr_milestone" to it },
            pr_draft?.let { "pr_draft" to "$it" },
            pr_allow_empty?.let { "pr_allow_empty" to "$it" },
            github_token?.let { "github_token" to it },
        ).toTypedArray()
    )
}
