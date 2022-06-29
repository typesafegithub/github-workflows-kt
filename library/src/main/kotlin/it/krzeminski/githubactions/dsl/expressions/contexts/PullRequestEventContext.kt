@file:Suppress("ObjectPropertyNaming")

package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.FakeList
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

public object PullRequestEventContextPullRequestUser :
    ExpressionContext("github.event.pull_request.user") {
    public const val login: String = "github.event.pull_request.user.login"

    public const val id: String = "github.event.pull_request.user.id"

    public const val node_id: String = "github.event.pull_request.user.node_id"

    public const val avatar_url: String = "github.event.pull_request.user.avatar_url"

    public const val gravatar_id: String = "github.event.pull_request.user.gravatar_id"

    public const val url: String = "github.event.pull_request.user.url"

    public const val html_url: String = "github.event.pull_request.user.html_url"

    public const val followers_url: String = "github.event.pull_request.user.followers_url"

    public const val following_url: String = "github.event.pull_request.user.following_url"

    public const val gists_url: String = "github.event.pull_request.user.gists_url"

    public const val starred_url: String = "github.event.pull_request.user.starred_url"

    public const val subscriptions_url: String = "github.event.pull_request.user.subscriptions_url"

    public const val organizations_url: String = "github.event.pull_request.user.organizations_url"

    public const val repos_url: String = "github.event.pull_request.user.repos_url"

    public const val events_url: String = "github.event.pull_request.user.events_url"

    public const val received_events_url: String =
        "github.event.pull_request.user.received_events_url"

    public const val type: String = "github.event.pull_request.user.type"

    public const val site_admin: String = "github.event.pull_request.user.site_admin"
}

public object PullRequestEventContextPullRequestHeadUser :
    ExpressionContext("github.event.pull_request.head.user") {
    public const val login: String = "github.event.pull_request.head.user.login"

    public const val id: String = "github.event.pull_request.head.user.id"

    public const val node_id: String = "github.event.pull_request.head.user.node_id"

    public const val avatar_url: String = "github.event.pull_request.head.user.avatar_url"

    public const val gravatar_id: String = "github.event.pull_request.head.user.gravatar_id"

    public const val url: String = "github.event.pull_request.head.user.url"

    public const val html_url: String = "github.event.pull_request.head.user.html_url"

    public const val followers_url: String = "github.event.pull_request.head.user.followers_url"

    public const val following_url: String = "github.event.pull_request.head.user.following_url"

    public const val gists_url: String = "github.event.pull_request.head.user.gists_url"

    public const val starred_url: String = "github.event.pull_request.head.user.starred_url"

    public const val subscriptions_url: String =
        "github.event.pull_request.head.user.subscriptions_url"

    public const val organizations_url: String =
        "github.event.pull_request.head.user.organizations_url"

    public const val repos_url: String = "github.event.pull_request.head.user.repos_url"

    public const val events_url: String = "github.event.pull_request.head.user.events_url"

    public const val received_events_url: String =
        "github.event.pull_request.head.user.received_events_url"

    public const val type: String = "github.event.pull_request.head.user.type"

    public const val site_admin: String = "github.event.pull_request.head.user.site_admin"
}

public object PullRequestEventContextPullRequestHeadRepoOwner :
    ExpressionContext("github.event.pull_request.head.repo.owner") {
    public const val login: String = "github.event.pull_request.head.repo.owner.login"

    public const val id: String = "github.event.pull_request.head.repo.owner.id"

    public const val node_id: String = "github.event.pull_request.head.repo.owner.node_id"

    public const val avatar_url: String = "github.event.pull_request.head.repo.owner.avatar_url"

    public const val gravatar_id: String = "github.event.pull_request.head.repo.owner.gravatar_id"

    public const val url: String = "github.event.pull_request.head.repo.owner.url"

    public const val html_url: String = "github.event.pull_request.head.repo.owner.html_url"

    public const val followers_url: String = "github.event.pull_request.head.repo.owner.followers_url"

    public const val following_url: String = "github.event.pull_request.head.repo.owner.following_url"

    public const val gists_url: String = "github.event.pull_request.head.repo.owner.gists_url"

    public const val starred_url: String = "github.event.pull_request.head.repo.owner.starred_url"

    public const val subscriptions_url: String =
        "github.event.pull_request.head.repo.owner.subscriptions_url"

    public const val organizations_url: String =
        "github.event.pull_request.head.repo.owner.organizations_url"

    public const val repos_url: String = "github.event.pull_request.head.repo.owner.repos_url"

    public const val events_url: String = "github.event.pull_request.head.repo.owner.events_url"

    public const val received_events_url: String =
        "github.event.pull_request.head.repo.owner.received_events_url"

    public const val type: String = "github.event.pull_request.head.repo.owner.type"

    public const val site_admin: String = "github.event.pull_request.head.repo.owner.site_admin"
}

public object PullRequestEventContextPullRequestHeadRepo :
    ExpressionContext("github.event.pull_request.head.repo") {
    public const val id: String = "github.event.pull_request.head.repo.id"

    public const val node_id: String = "github.event.pull_request.head.repo.node_id"

    public const val name: String = "github.event.pull_request.head.repo.name"

    public const val full_name: String = "github.event.pull_request.head.repo.full_name"

    public const val `private`: String = "github.event.pull_request.head.repo.private"

    public val owner: PullRequestEventContextPullRequestHeadRepoOwner =
        PullRequestEventContextPullRequestHeadRepoOwner

    public const val html_url: String = "github.event.pull_request.head.repo.html_url"

    public const val description: String = "github.event.pull_request.head.repo.description"

    public const val fork: String = "github.event.pull_request.head.repo.fork"

    public const val url: String = "github.event.pull_request.head.repo.url"

    public const val forks_url: String = "github.event.pull_request.head.repo.forks_url"

    public const val keys_url: String = "github.event.pull_request.head.repo.keys_url"

    public const val collaborators_url: String =
        "github.event.pull_request.head.repo.collaborators_url"

    public const val teams_url: String = "github.event.pull_request.head.repo.teams_url"

    public const val hooks_url: String = "github.event.pull_request.head.repo.hooks_url"

    public const val issue_events_url: String = "github.event.pull_request.head.repo.issue_events_url"

    public const val events_url: String = "github.event.pull_request.head.repo.events_url"

    public const val assignees_url: String = "github.event.pull_request.head.repo.assignees_url"

    public const val branches_url: String = "github.event.pull_request.head.repo.branches_url"

    public const val tags_url: String = "github.event.pull_request.head.repo.tags_url"

    public const val blobs_url: String = "github.event.pull_request.head.repo.blobs_url"

    public const val git_tags_url: String = "github.event.pull_request.head.repo.git_tags_url"

    public const val git_refs_url: String = "github.event.pull_request.head.repo.git_refs_url"

    public const val trees_url: String = "github.event.pull_request.head.repo.trees_url"

    public const val statuses_url: String = "github.event.pull_request.head.repo.statuses_url"

    public const val languages_url: String = "github.event.pull_request.head.repo.languages_url"

    public const val stargazers_url: String = "github.event.pull_request.head.repo.stargazers_url"

    public const val contributors_url: String = "github.event.pull_request.head.repo.contributors_url"

    public const val subscribers_url: String = "github.event.pull_request.head.repo.subscribers_url"

    public const val subscription_url: String = "github.event.pull_request.head.repo.subscription_url"

    public const val commits_url: String = "github.event.pull_request.head.repo.commits_url"

    public const val git_commits_url: String = "github.event.pull_request.head.repo.git_commits_url"

    public const val comments_url: String = "github.event.pull_request.head.repo.comments_url"

    public const val issue_comment_url: String =
        "github.event.pull_request.head.repo.issue_comment_url"

    public const val contents_url: String = "github.event.pull_request.head.repo.contents_url"

    public const val compare_url: String = "github.event.pull_request.head.repo.compare_url"

    public const val merges_url: String = "github.event.pull_request.head.repo.merges_url"

    public const val archive_url: String = "github.event.pull_request.head.repo.archive_url"

    public const val downloads_url: String = "github.event.pull_request.head.repo.downloads_url"

    public const val issues_url: String = "github.event.pull_request.head.repo.issues_url"

    public const val pulls_url: String = "github.event.pull_request.head.repo.pulls_url"

    public const val milestones_url: String = "github.event.pull_request.head.repo.milestones_url"

    public const val notifications_url: String =
        "github.event.pull_request.head.repo.notifications_url"

    public const val labels_url: String = "github.event.pull_request.head.repo.labels_url"

    public const val releases_url: String = "github.event.pull_request.head.repo.releases_url"

    public const val deployments_url: String = "github.event.pull_request.head.repo.deployments_url"

    public const val created_at: String = "github.event.pull_request.head.repo.created_at"

    public const val updated_at: String = "github.event.pull_request.head.repo.updated_at"

    public const val pushed_at: String = "github.event.pull_request.head.repo.pushed_at"

    public const val git_url: String = "github.event.pull_request.head.repo.git_url"

    public const val ssh_url: String = "github.event.pull_request.head.repo.ssh_url"

    public const val clone_url: String = "github.event.pull_request.head.repo.clone_url"

    public const val svn_url: String = "github.event.pull_request.head.repo.svn_url"

    public const val homepage: String = "github.event.pull_request.head.repo.homepage"

    public const val length: String = "github.event.pull_request.head.repo.size"

    public const val stargazers_count: String = "github.event.pull_request.head.repo.stargazers_count"

    public const val watchers_count: String = "github.event.pull_request.head.repo.watchers_count"

    public const val language: String = "github.event.pull_request.head.repo.language"

    public const val has_issues: String = "github.event.pull_request.head.repo.has_issues"

    public const val has_projects: String = "github.event.pull_request.head.repo.has_projects"

    public const val has_downloads: String = "github.event.pull_request.head.repo.has_downloads"

    public const val has_wiki: String = "github.event.pull_request.head.repo.has_wiki"

    public const val has_pages: String = "github.event.pull_request.head.repo.has_pages"

    public const val forks_count: String = "github.event.pull_request.head.repo.forks_count"

    public const val mirror_url: String = "github.event.pull_request.head.repo.mirror_url"

    public const val archived: String = "github.event.pull_request.head.repo.archived"

    public const val disabled: String = "github.event.pull_request.head.repo.disabled"

    public const val open_issues_count: String =
        "github.event.pull_request.head.repo.open_issues_count"

    public const val license: String = "github.event.pull_request.head.repo.license"

    public const val forks: String = "github.event.pull_request.head.repo.forks"

    public const val open_issues: String = "github.event.pull_request.head.repo.open_issues"

    public const val watchers: String = "github.event.pull_request.head.repo.watchers"

    public const val default_branch: String = "github.event.pull_request.head.repo.default_branch"

    public const val allow_squash_merge: String =
        "github.event.pull_request.head.repo.allow_squash_merge"

    public const val allow_merge_commit: String =
        "github.event.pull_request.head.repo.allow_merge_commit"

    public const val allow_rebase_merge: String =
        "github.event.pull_request.head.repo.allow_rebase_merge"

    public const val delete_branch_on_merge: String =
        "github.event.pull_request.head.repo.delete_branch_on_merge"
}

public object PullRequestEventContextPullRequestHead :
    ExpressionContext("github.event.pull_request.head") {
    public const val label: String = "github.event.pull_request.head.label"

    public const val ref: String = "github.event.pull_request.head.ref"

    public const val sha: String = "github.event.pull_request.head.sha"

    public val user: PullRequestEventContextPullRequestHeadUser =
        PullRequestEventContextPullRequestHeadUser

    public val repo: PullRequestEventContextPullRequestHeadRepo =
        PullRequestEventContextPullRequestHeadRepo
}

public object PullRequestEventContextPullRequestBaseUser :
    ExpressionContext("github.event.pull_request.base.user") {
    public const val login: String = "github.event.pull_request.base.user.login"

    public const val id: String = "github.event.pull_request.base.user.id"

    public const val node_id: String = "github.event.pull_request.base.user.node_id"

    public const val avatar_url: String = "github.event.pull_request.base.user.avatar_url"

    public const val gravatar_id: String = "github.event.pull_request.base.user.gravatar_id"

    public const val url: String = "github.event.pull_request.base.user.url"

    public const val html_url: String = "github.event.pull_request.base.user.html_url"

    public const val followers_url: String = "github.event.pull_request.base.user.followers_url"

    public const val following_url: String = "github.event.pull_request.base.user.following_url"

    public const val gists_url: String = "github.event.pull_request.base.user.gists_url"

    public const val starred_url: String = "github.event.pull_request.base.user.starred_url"

    public const val subscriptions_url: String =
        "github.event.pull_request.base.user.subscriptions_url"

    public const val organizations_url: String =
        "github.event.pull_request.base.user.organizations_url"

    public const val repos_url: String = "github.event.pull_request.base.user.repos_url"

    public const val events_url: String = "github.event.pull_request.base.user.events_url"

    public const val received_events_url: String =
        "github.event.pull_request.base.user.received_events_url"

    public const val type: String = "github.event.pull_request.base.user.type"

    public const val site_admin: String = "github.event.pull_request.base.user.site_admin"
}

public object PullRequestEventContextPullRequestBaseRepoOwner :
    ExpressionContext("github.event.pull_request.base.repo.owner") {
    public const val login: String = "github.event.pull_request.base.repo.owner.login"

    public const val id: String = "github.event.pull_request.base.repo.owner.id"

    public const val node_id: String = "github.event.pull_request.base.repo.owner.node_id"

    public const val avatar_url: String = "github.event.pull_request.base.repo.owner.avatar_url"

    public const val gravatar_id: String = "github.event.pull_request.base.repo.owner.gravatar_id"

    public const val url: String = "github.event.pull_request.base.repo.owner.url"

    public const val html_url: String = "github.event.pull_request.base.repo.owner.html_url"

    public const val followers_url: String = "github.event.pull_request.base.repo.owner.followers_url"

    public const val following_url: String = "github.event.pull_request.base.repo.owner.following_url"

    public const val gists_url: String = "github.event.pull_request.base.repo.owner.gists_url"

    public const val starred_url: String = "github.event.pull_request.base.repo.owner.starred_url"

    public const val subscriptions_url: String =
        "github.event.pull_request.base.repo.owner.subscriptions_url"

    public const val organizations_url: String =
        "github.event.pull_request.base.repo.owner.organizations_url"

    public const val repos_url: String = "github.event.pull_request.base.repo.owner.repos_url"

    public const val events_url: String = "github.event.pull_request.base.repo.owner.events_url"

    public const val received_events_url: String =
        "github.event.pull_request.base.repo.owner.received_events_url"

    public const val type: String = "github.event.pull_request.base.repo.owner.type"

    public const val site_admin: String = "github.event.pull_request.base.repo.owner.site_admin"
}

public object PullRequestEventContextPullRequestBaseRepo :
    ExpressionContext("github.event.pull_request.base.repo") {
    public const val id: String = "github.event.pull_request.base.repo.id"

    public const val node_id: String = "github.event.pull_request.base.repo.node_id"

    public const val name: String = "github.event.pull_request.base.repo.name"

    public const val full_name: String = "github.event.pull_request.base.repo.full_name"

    public const val `private`: String = "github.event.pull_request.base.repo.private"

    public val owner: PullRequestEventContextPullRequestBaseRepoOwner =
        PullRequestEventContextPullRequestBaseRepoOwner

    public const val html_url: String = "github.event.pull_request.base.repo.html_url"

    public const val description: String = "github.event.pull_request.base.repo.description"

    public const val fork: String = "github.event.pull_request.base.repo.fork"

    public const val url: String = "github.event.pull_request.base.repo.url"

    public const val forks_url: String = "github.event.pull_request.base.repo.forks_url"

    public const val keys_url: String = "github.event.pull_request.base.repo.keys_url"

    public const val collaborators_url: String =
        "github.event.pull_request.base.repo.collaborators_url"

    public const val teams_url: String = "github.event.pull_request.base.repo.teams_url"

    public const val hooks_url: String = "github.event.pull_request.base.repo.hooks_url"

    public const val issue_events_url: String = "github.event.pull_request.base.repo.issue_events_url"

    public const val events_url: String = "github.event.pull_request.base.repo.events_url"

    public const val assignees_url: String = "github.event.pull_request.base.repo.assignees_url"

    public const val branches_url: String = "github.event.pull_request.base.repo.branches_url"

    public const val tags_url: String = "github.event.pull_request.base.repo.tags_url"

    public const val blobs_url: String = "github.event.pull_request.base.repo.blobs_url"

    public const val git_tags_url: String = "github.event.pull_request.base.repo.git_tags_url"

    public const val git_refs_url: String = "github.event.pull_request.base.repo.git_refs_url"

    public const val trees_url: String = "github.event.pull_request.base.repo.trees_url"

    public const val statuses_url: String = "github.event.pull_request.base.repo.statuses_url"

    public const val languages_url: String = "github.event.pull_request.base.repo.languages_url"

    public const val stargazers_url: String = "github.event.pull_request.base.repo.stargazers_url"

    public const val contributors_url: String = "github.event.pull_request.base.repo.contributors_url"

    public const val subscribers_url: String = "github.event.pull_request.base.repo.subscribers_url"

    public const val subscription_url: String = "github.event.pull_request.base.repo.subscription_url"

    public const val commits_url: String = "github.event.pull_request.base.repo.commits_url"

    public const val git_commits_url: String = "github.event.pull_request.base.repo.git_commits_url"

    public const val comments_url: String = "github.event.pull_request.base.repo.comments_url"

    public const val issue_comment_url: String =
        "github.event.pull_request.base.repo.issue_comment_url"

    public const val contents_url: String = "github.event.pull_request.base.repo.contents_url"

    public const val compare_url: String = "github.event.pull_request.base.repo.compare_url"

    public const val merges_url: String = "github.event.pull_request.base.repo.merges_url"

    public const val archive_url: String = "github.event.pull_request.base.repo.archive_url"

    public const val downloads_url: String = "github.event.pull_request.base.repo.downloads_url"

    public const val issues_url: String = "github.event.pull_request.base.repo.issues_url"

    public const val pulls_url: String = "github.event.pull_request.base.repo.pulls_url"

    public const val milestones_url: String = "github.event.pull_request.base.repo.milestones_url"

    public const val notifications_url: String =
        "github.event.pull_request.base.repo.notifications_url"

    public const val labels_url: String = "github.event.pull_request.base.repo.labels_url"

    public const val releases_url: String = "github.event.pull_request.base.repo.releases_url"

    public const val deployments_url: String = "github.event.pull_request.base.repo.deployments_url"

    public const val created_at: String = "github.event.pull_request.base.repo.created_at"

    public const val updated_at: String = "github.event.pull_request.base.repo.updated_at"

    public const val pushed_at: String = "github.event.pull_request.base.repo.pushed_at"

    public const val git_url: String = "github.event.pull_request.base.repo.git_url"

    public const val ssh_url: String = "github.event.pull_request.base.repo.ssh_url"

    public const val clone_url: String = "github.event.pull_request.base.repo.clone_url"

    public const val svn_url: String = "github.event.pull_request.base.repo.svn_url"

    public const val homepage: String = "github.event.pull_request.base.repo.homepage"

    public const val length: String = "github.event.pull_request.base.repo.size"

    public const val stargazers_count: String = "github.event.pull_request.base.repo.stargazers_count"

    public const val watchers_count: String = "github.event.pull_request.base.repo.watchers_count"

    public const val language: String = "github.event.pull_request.base.repo.language"

    public const val has_issues: String = "github.event.pull_request.base.repo.has_issues"

    public const val has_projects: String = "github.event.pull_request.base.repo.has_projects"

    public const val has_downloads: String = "github.event.pull_request.base.repo.has_downloads"

    public const val has_wiki: String = "github.event.pull_request.base.repo.has_wiki"

    public const val has_pages: String = "github.event.pull_request.base.repo.has_pages"

    public const val forks_count: String = "github.event.pull_request.base.repo.forks_count"

    public const val mirror_url: String = "github.event.pull_request.base.repo.mirror_url"

    public const val archived: String = "github.event.pull_request.base.repo.archived"

    public const val disabled: String = "github.event.pull_request.base.repo.disabled"

    public const val open_issues_count: String =
        "github.event.pull_request.base.repo.open_issues_count"

    public const val license: String = "github.event.pull_request.base.repo.license"

    public const val forks: String = "github.event.pull_request.base.repo.forks"

    public const val open_issues: String = "github.event.pull_request.base.repo.open_issues"

    public const val watchers: String = "github.event.pull_request.base.repo.watchers"

    public const val default_branch: String = "github.event.pull_request.base.repo.default_branch"

    public const val allow_squash_merge: String =
        "github.event.pull_request.base.repo.allow_squash_merge"

    public const val allow_merge_commit: String =
        "github.event.pull_request.base.repo.allow_merge_commit"

    public const val allow_rebase_merge: String =
        "github.event.pull_request.base.repo.allow_rebase_merge"

    public const val delete_branch_on_merge: String =
        "github.event.pull_request.base.repo.delete_branch_on_merge"
}

public object PullRequestEventContextPullRequestBase :
    ExpressionContext("github.event.pull_request.base") {
    public const val label: String = "github.event.pull_request.base.label"

    public const val ref: String = "github.event.pull_request.base.ref"

    public const val sha: String = "github.event.pull_request.base.sha"

    public val user: PullRequestEventContextPullRequestBaseUser =
        PullRequestEventContextPullRequestBaseUser

    public val repo: PullRequestEventContextPullRequestBaseRepo =
        PullRequestEventContextPullRequestBaseRepo
}

public object PullRequestEventContextPullRequestLinksSelf :
    ExpressionContext("github.event.pull_request._links.self") {
    public const val href: String = "github.event.pull_request._links.self.href"
}

public object PullRequestEventContextPullRequestLinksHtml :
    ExpressionContext("github.event.pull_request._links.html") {
    public const val href: String = "github.event.pull_request._links.html.href"
}

public object PullRequestEventContextPullRequestLinksIssue :
    ExpressionContext("github.event.pull_request._links.issue") {
    public const val href: String = "github.event.pull_request._links.issue.href"
}

public object PullRequestEventContextPullRequestLinksComments :
    ExpressionContext("github.event.pull_request._links.comments") {
    public const val href: String = "github.event.pull_request._links.comments.href"
}

public object PullRequestEventContextPullRequestLinksReviewComments :
    ExpressionContext("github.event.pull_request._links.review_comments") {
    public const val href: String = "github.event.pull_request._links.review_comments.href"
}

public object PullRequestEventContextPullRequestLinksReviewComment :
    ExpressionContext("github.event.pull_request._links.review_comment") {
    public const val href: String = "github.event.pull_request._links.review_comment.href"
}

public object PullRequestEventContextPullRequestLinksCommits :
    ExpressionContext("github.event.pull_request._links.commits") {
    public const val href: String = "github.event.pull_request._links.commits.href"
}

public object PullRequestEventContextPullRequestLinksStatuses :
    ExpressionContext("github.event.pull_request._links.statuses") {
    public const val href: String = "github.event.pull_request._links.statuses.href"
}

public object PullRequestEventContextPullRequestLinks :
    ExpressionContext("github.event.pull_request._links") {
    public val self: PullRequestEventContextPullRequestLinksSelf =
        PullRequestEventContextPullRequestLinksSelf

    public val html: PullRequestEventContextPullRequestLinksHtml =
        PullRequestEventContextPullRequestLinksHtml

    public val issue: PullRequestEventContextPullRequestLinksIssue =
        PullRequestEventContextPullRequestLinksIssue

    public val comments: PullRequestEventContextPullRequestLinksComments =
        PullRequestEventContextPullRequestLinksComments

    public val review_comments: PullRequestEventContextPullRequestLinksReviewComments =
        PullRequestEventContextPullRequestLinksReviewComments

    public val review_comment: PullRequestEventContextPullRequestLinksReviewComment =
        PullRequestEventContextPullRequestLinksReviewComment

    public val commits: PullRequestEventContextPullRequestLinksCommits =
        PullRequestEventContextPullRequestLinksCommits

    public val statuses: PullRequestEventContextPullRequestLinksStatuses =
        PullRequestEventContextPullRequestLinksStatuses
}

public object PullRequestEventContextPullRequest : ExpressionContext("github.event.pull_request") {
    public const val url: String = "github.event.pull_request.url"

    public const val id: String = "github.event.pull_request.id"

    public const val node_id: String = "github.event.pull_request.node_id"

    public const val html_url: String = "github.event.pull_request.html_url"

    public const val diff_url: String = "github.event.pull_request.diff_url"

    public const val patch_url: String = "github.event.pull_request.patch_url"

    public const val issue_url: String = "github.event.pull_request.issue_url"

    public const val number: String = "github.event.pull_request.number"

    public const val state: String = "github.event.pull_request.state"

    public const val locked: String = "github.event.pull_request.locked"

    public const val title: String = "github.event.pull_request.title"

    public val user: PullRequestEventContextPullRequestUser = PullRequestEventContextPullRequestUser

    public const val body: String = "github.event.pull_request.body"

    public const val created_at: String = "github.event.pull_request.created_at"

    public const val updated_at: String = "github.event.pull_request.updated_at"

    public const val closed_at: String = "github.event.pull_request.closed_at"

    public const val merged_at: String = "github.event.pull_request.merged_at"

    public const val merge_commit_sha: String = "github.event.pull_request.merge_commit_sha"

    public const val assignee: String = "github.event.pull_request.assignee"

    public val assignees: List<String> = FakeList("github.event.pull_request.assignees")

    public val requested_reviewers: List<String> =
        FakeList("github.event.pull_request.requested_reviewers")

    public val requested_teams: List<String> = FakeList("github.event.pull_request.requested_teams")

    public val labels: List<String> = FakeList("github.event.pull_request.labels")

    public const val milestone: String = "github.event.pull_request.milestone"

    public const val commits_url: String = "github.event.pull_request.commits_url"

    public const val review_comments_url: String = "github.event.pull_request.review_comments_url"

    public const val review_comment_url: String = "github.event.pull_request.review_comment_url"

    public const val comments_url: String = "github.event.pull_request.comments_url"

    public const val statuses_url: String = "github.event.pull_request.statuses_url"

    public val head: PullRequestEventContextPullRequestHead = PullRequestEventContextPullRequestHead

    public val base: PullRequestEventContextPullRequestBase = PullRequestEventContextPullRequestBase

    public val _links: PullRequestEventContextPullRequestLinks =
        PullRequestEventContextPullRequestLinks

    public const val author_association: String = "github.event.pull_request.author_association"

    public const val draft: String = "github.event.pull_request.draft"

    public const val merged: String = "github.event.pull_request.merged"

    public const val mergeable: String = "github.event.pull_request.mergeable"

    public const val rebaseable: String = "github.event.pull_request.rebaseable"

    public const val mergeable_state: String = "github.event.pull_request.mergeable_state"

    public const val merged_by: String = "github.event.pull_request.merged_by"

    public const val comments: String = "github.event.pull_request.comments"

    public const val review_comments: String = "github.event.pull_request.review_comments"

    public const val maintainer_can_modify: String = "github.event.pull_request.maintainer_can_modify"

    public const val commits: String = "github.event.pull_request.commits"

    public const val additions: String = "github.event.pull_request.additions"

    public const val deletions: String = "github.event.pull_request.deletions"

    public const val changed_files: String = "github.event.pull_request.changed_files"
}

public object PullRequestEventContextRepositoryOwner :
    ExpressionContext("github.event.repository.owner") {
    public const val login: String = "github.event.repository.owner.login"

    public const val id: String = "github.event.repository.owner.id"

    public const val node_id: String = "github.event.repository.owner.node_id"

    public const val avatar_url: String = "github.event.repository.owner.avatar_url"

    public const val gravatar_id: String = "github.event.repository.owner.gravatar_id"

    public const val url: String = "github.event.repository.owner.url"

    public const val html_url: String = "github.event.repository.owner.html_url"

    public const val followers_url: String = "github.event.repository.owner.followers_url"

    public const val following_url: String = "github.event.repository.owner.following_url"

    public const val gists_url: String = "github.event.repository.owner.gists_url"

    public const val starred_url: String = "github.event.repository.owner.starred_url"

    public const val subscriptions_url: String = "github.event.repository.owner.subscriptions_url"

    public const val organizations_url: String = "github.event.repository.owner.organizations_url"

    public const val repos_url: String = "github.event.repository.owner.repos_url"

    public const val events_url: String = "github.event.repository.owner.events_url"

    public const val received_events_url: String = "github.event.repository.owner.received_events_url"

    public const val type: String = "github.event.repository.owner.type"

    public const val site_admin: String = "github.event.repository.owner.site_admin"
}

public object PullRequestEventContextRepository : ExpressionContext("github.event.repository") {
    public const val id: String = "github.event.repository.id"

    public const val node_id: String = "github.event.repository.node_id"

    public const val name: String = "github.event.repository.name"

    public const val full_name: String = "github.event.repository.full_name"

    public const val `private`: String = "github.event.repository.private"

    public val owner: PullRequestEventContextRepositoryOwner = PullRequestEventContextRepositoryOwner

    public const val html_url: String = "github.event.repository.html_url"

    public const val description: String = "github.event.repository.description"

    public const val fork: String = "github.event.repository.fork"

    public const val url: String = "github.event.repository.url"

    public const val forks_url: String = "github.event.repository.forks_url"

    public const val keys_url: String = "github.event.repository.keys_url"

    public const val collaborators_url: String = "github.event.repository.collaborators_url"

    public const val teams_url: String = "github.event.repository.teams_url"

    public const val hooks_url: String = "github.event.repository.hooks_url"

    public const val issue_events_url: String = "github.event.repository.issue_events_url"

    public const val events_url: String = "github.event.repository.events_url"

    public const val assignees_url: String = "github.event.repository.assignees_url"

    public const val branches_url: String = "github.event.repository.branches_url"

    public const val tags_url: String = "github.event.repository.tags_url"

    public const val blobs_url: String = "github.event.repository.blobs_url"

    public const val git_tags_url: String = "github.event.repository.git_tags_url"

    public const val git_refs_url: String = "github.event.repository.git_refs_url"

    public const val trees_url: String = "github.event.repository.trees_url"

    public const val statuses_url: String = "github.event.repository.statuses_url"

    public const val languages_url: String = "github.event.repository.languages_url"

    public const val stargazers_url: String = "github.event.repository.stargazers_url"

    public const val contributors_url: String = "github.event.repository.contributors_url"

    public const val subscribers_url: String = "github.event.repository.subscribers_url"

    public const val subscription_url: String = "github.event.repository.subscription_url"

    public const val commits_url: String = "github.event.repository.commits_url"

    public const val git_commits_url: String = "github.event.repository.git_commits_url"

    public const val comments_url: String = "github.event.repository.comments_url"

    public const val issue_comment_url: String = "github.event.repository.issue_comment_url"

    public const val contents_url: String = "github.event.repository.contents_url"

    public const val compare_url: String = "github.event.repository.compare_url"

    public const val merges_url: String = "github.event.repository.merges_url"

    public const val archive_url: String = "github.event.repository.archive_url"

    public const val downloads_url: String = "github.event.repository.downloads_url"

    public const val issues_url: String = "github.event.repository.issues_url"

    public const val pulls_url: String = "github.event.repository.pulls_url"

    public const val milestones_url: String = "github.event.repository.milestones_url"

    public const val notifications_url: String = "github.event.repository.notifications_url"

    public const val labels_url: String = "github.event.repository.labels_url"

    public const val releases_url: String = "github.event.repository.releases_url"

    public const val deployments_url: String = "github.event.repository.deployments_url"

    public const val created_at: String = "github.event.repository.created_at"

    public const val updated_at: String = "github.event.repository.updated_at"

    public const val pushed_at: String = "github.event.repository.pushed_at"

    public const val git_url: String = "github.event.repository.git_url"

    public const val ssh_url: String = "github.event.repository.ssh_url"

    public const val clone_url: String = "github.event.repository.clone_url"

    public const val svn_url: String = "github.event.repository.svn_url"

    public const val homepage: String = "github.event.repository.homepage"

    public const val length: String = "github.event.repository.size"

    public const val stargazers_count: String = "github.event.repository.stargazers_count"

    public const val watchers_count: String = "github.event.repository.watchers_count"

    public const val language: String = "github.event.repository.language"

    public const val has_issues: String = "github.event.repository.has_issues"

    public const val has_projects: String = "github.event.repository.has_projects"

    public const val has_downloads: String = "github.event.repository.has_downloads"

    public const val has_wiki: String = "github.event.repository.has_wiki"

    public const val has_pages: String = "github.event.repository.has_pages"

    public const val forks_count: String = "github.event.repository.forks_count"

    public const val mirror_url: String = "github.event.repository.mirror_url"

    public const val archived: String = "github.event.repository.archived"

    public const val disabled: String = "github.event.repository.disabled"

    public const val open_issues_count: String = "github.event.repository.open_issues_count"

    public const val license: String = "github.event.repository.license"

    public const val forks: String = "github.event.repository.forks"

    public const val open_issues: String = "github.event.repository.open_issues"

    public const val watchers: String = "github.event.repository.watchers"

    public const val default_branch: String = "github.event.repository.default_branch"
}

public object PullRequestEventContextSender : ExpressionContext("github.event.sender") {
    public const val login: String = "github.event.sender.login"

    public const val id: String = "github.event.sender.id"

    public const val node_id: String = "github.event.sender.node_id"

    public const val avatar_url: String = "github.event.sender.avatar_url"

    public const val gravatar_id: String = "github.event.sender.gravatar_id"

    public const val url: String = "github.event.sender.url"

    public const val html_url: String = "github.event.sender.html_url"

    public const val followers_url: String = "github.event.sender.followers_url"

    public const val following_url: String = "github.event.sender.following_url"

    public const val gists_url: String = "github.event.sender.gists_url"

    public const val starred_url: String = "github.event.sender.starred_url"

    public const val subscriptions_url: String = "github.event.sender.subscriptions_url"

    public const val organizations_url: String = "github.event.sender.organizations_url"

    public const val repos_url: String = "github.event.sender.repos_url"

    public const val events_url: String = "github.event.sender.events_url"

    public const val received_events_url: String = "github.event.sender.received_events_url"

    public const val type: String = "github.event.sender.type"

    public const val site_admin: String = "github.event.sender.site_admin"
}

public object PullRequestEventContext : ExpressionContext("github.event") {
    public const val action: String = "github.event.action"

    public const val number: String = "github.event.number"

    public val pull_request: PullRequestEventContextPullRequest = PullRequestEventContextPullRequest

    public val repository: PullRequestEventContextRepository = PullRequestEventContextRepository

    public val sender: PullRequestEventContextSender = PullRequestEventContextSender
}
