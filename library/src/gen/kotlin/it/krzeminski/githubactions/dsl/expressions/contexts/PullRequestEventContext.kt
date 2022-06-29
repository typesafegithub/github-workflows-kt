package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.FakeList
import kotlin.String
import kotlin.collections.List

public object PullRequestEventContextPullRequestUser :
    ExpressionContext("github.event.pull_request.user") {
    public val login: String = "github.event.pull_request.user.login"

    public val id: String = "github.event.pull_request.user.id"

    public val node_id: String = "github.event.pull_request.user.node_id"

    public val avatar_url: String = "github.event.pull_request.user.avatar_url"

    public val gravatar_id: String = "github.event.pull_request.user.gravatar_id"

    public val url: String = "github.event.pull_request.user.url"

    public val html_url: String = "github.event.pull_request.user.html_url"

    public val followers_url: String = "github.event.pull_request.user.followers_url"

    public val following_url: String = "github.event.pull_request.user.following_url"

    public val gists_url: String = "github.event.pull_request.user.gists_url"

    public val starred_url: String = "github.event.pull_request.user.starred_url"

    public val subscriptions_url: String = "github.event.pull_request.user.subscriptions_url"

    public val organizations_url: String = "github.event.pull_request.user.organizations_url"

    public val repos_url: String = "github.event.pull_request.user.repos_url"

    public val events_url: String = "github.event.pull_request.user.events_url"

    public val received_events_url: String = "github.event.pull_request.user.received_events_url"

    public val type: String = "github.event.pull_request.user.type"

    public val site_admin: String = "github.event.pull_request.user.site_admin"
}

public object PullRequestEventContextPullRequestHeadUser :
    ExpressionContext("github.event.pull_request.head.user") {
    public val login: String = "github.event.pull_request.head.user.login"

    public val id: String = "github.event.pull_request.head.user.id"

    public val node_id: String = "github.event.pull_request.head.user.node_id"

    public val avatar_url: String = "github.event.pull_request.head.user.avatar_url"

    public val gravatar_id: String = "github.event.pull_request.head.user.gravatar_id"

    public val url: String = "github.event.pull_request.head.user.url"

    public val html_url: String = "github.event.pull_request.head.user.html_url"

    public val followers_url: String = "github.event.pull_request.head.user.followers_url"

    public val following_url: String = "github.event.pull_request.head.user.following_url"

    public val gists_url: String = "github.event.pull_request.head.user.gists_url"

    public val starred_url: String = "github.event.pull_request.head.user.starred_url"

    public val subscriptions_url: String = "github.event.pull_request.head.user.subscriptions_url"

    public val organizations_url: String = "github.event.pull_request.head.user.organizations_url"

    public val repos_url: String = "github.event.pull_request.head.user.repos_url"

    public val events_url: String = "github.event.pull_request.head.user.events_url"

    public val received_events_url: String = "github.event.pull_request.head.user.received_events_url"

    public val type: String = "github.event.pull_request.head.user.type"

    public val site_admin: String = "github.event.pull_request.head.user.site_admin"
}

public object PullRequestEventContextPullRequestHeadRepoOwner :
    ExpressionContext("github.event.pull_request.head.repo.owner") {
    public val login: String = "github.event.pull_request.head.repo.owner.login"

    public val id: String = "github.event.pull_request.head.repo.owner.id"

    public val node_id: String = "github.event.pull_request.head.repo.owner.node_id"

    public val avatar_url: String = "github.event.pull_request.head.repo.owner.avatar_url"

    public val gravatar_id: String = "github.event.pull_request.head.repo.owner.gravatar_id"

    public val url: String = "github.event.pull_request.head.repo.owner.url"

    public val html_url: String = "github.event.pull_request.head.repo.owner.html_url"

    public val followers_url: String = "github.event.pull_request.head.repo.owner.followers_url"

    public val following_url: String = "github.event.pull_request.head.repo.owner.following_url"

    public val gists_url: String = "github.event.pull_request.head.repo.owner.gists_url"

    public val starred_url: String = "github.event.pull_request.head.repo.owner.starred_url"

    public val subscriptions_url: String =
        "github.event.pull_request.head.repo.owner.subscriptions_url"

    public val organizations_url: String =
        "github.event.pull_request.head.repo.owner.organizations_url"

    public val repos_url: String = "github.event.pull_request.head.repo.owner.repos_url"

    public val events_url: String = "github.event.pull_request.head.repo.owner.events_url"

    public val received_events_url: String =
        "github.event.pull_request.head.repo.owner.received_events_url"

    public val type: String = "github.event.pull_request.head.repo.owner.type"

    public val site_admin: String = "github.event.pull_request.head.repo.owner.site_admin"
}

public object PullRequestEventContextPullRequestHeadRepo :
    ExpressionContext("github.event.pull_request.head.repo") {
    public val id: String = "github.event.pull_request.head.repo.id"

    public val node_id: String = "github.event.pull_request.head.repo.node_id"

    public val name: String = "github.event.pull_request.head.repo.name"

    public val full_name: String = "github.event.pull_request.head.repo.full_name"

    public val `private`: String = "github.event.pull_request.head.repo.private"

    public val owner: PullRequestEventContextPullRequestHeadRepoOwner =
        PullRequestEventContextPullRequestHeadRepoOwner

    public val html_url: String = "github.event.pull_request.head.repo.html_url"

    public val description: String = "github.event.pull_request.head.repo.description"

    public val fork: String = "github.event.pull_request.head.repo.fork"

    public val url: String = "github.event.pull_request.head.repo.url"

    public val forks_url: String = "github.event.pull_request.head.repo.forks_url"

    public val keys_url: String = "github.event.pull_request.head.repo.keys_url"

    public val collaborators_url: String = "github.event.pull_request.head.repo.collaborators_url"

    public val teams_url: String = "github.event.pull_request.head.repo.teams_url"

    public val hooks_url: String = "github.event.pull_request.head.repo.hooks_url"

    public val issue_events_url: String = "github.event.pull_request.head.repo.issue_events_url"

    public val events_url: String = "github.event.pull_request.head.repo.events_url"

    public val assignees_url: String = "github.event.pull_request.head.repo.assignees_url"

    public val branches_url: String = "github.event.pull_request.head.repo.branches_url"

    public val tags_url: String = "github.event.pull_request.head.repo.tags_url"

    public val blobs_url: String = "github.event.pull_request.head.repo.blobs_url"

    public val git_tags_url: String = "github.event.pull_request.head.repo.git_tags_url"

    public val git_refs_url: String = "github.event.pull_request.head.repo.git_refs_url"

    public val trees_url: String = "github.event.pull_request.head.repo.trees_url"

    public val statuses_url: String = "github.event.pull_request.head.repo.statuses_url"

    public val languages_url: String = "github.event.pull_request.head.repo.languages_url"

    public val stargazers_url: String = "github.event.pull_request.head.repo.stargazers_url"

    public val contributors_url: String = "github.event.pull_request.head.repo.contributors_url"

    public val subscribers_url: String = "github.event.pull_request.head.repo.subscribers_url"

    public val subscription_url: String = "github.event.pull_request.head.repo.subscription_url"

    public val commits_url: String = "github.event.pull_request.head.repo.commits_url"

    public val git_commits_url: String = "github.event.pull_request.head.repo.git_commits_url"

    public val comments_url: String = "github.event.pull_request.head.repo.comments_url"

    public val issue_comment_url: String = "github.event.pull_request.head.repo.issue_comment_url"

    public val contents_url: String = "github.event.pull_request.head.repo.contents_url"

    public val compare_url: String = "github.event.pull_request.head.repo.compare_url"

    public val merges_url: String = "github.event.pull_request.head.repo.merges_url"

    public val archive_url: String = "github.event.pull_request.head.repo.archive_url"

    public val downloads_url: String = "github.event.pull_request.head.repo.downloads_url"

    public val issues_url: String = "github.event.pull_request.head.repo.issues_url"

    public val pulls_url: String = "github.event.pull_request.head.repo.pulls_url"

    public val milestones_url: String = "github.event.pull_request.head.repo.milestones_url"

    public val notifications_url: String = "github.event.pull_request.head.repo.notifications_url"

    public val labels_url: String = "github.event.pull_request.head.repo.labels_url"

    public val releases_url: String = "github.event.pull_request.head.repo.releases_url"

    public val deployments_url: String = "github.event.pull_request.head.repo.deployments_url"

    public val created_at: String = "github.event.pull_request.head.repo.created_at"

    public val updated_at: String = "github.event.pull_request.head.repo.updated_at"

    public val pushed_at: String = "github.event.pull_request.head.repo.pushed_at"

    public val git_url: String = "github.event.pull_request.head.repo.git_url"

    public val ssh_url: String = "github.event.pull_request.head.repo.ssh_url"

    public val clone_url: String = "github.event.pull_request.head.repo.clone_url"

    public val svn_url: String = "github.event.pull_request.head.repo.svn_url"

    public val homepage: String = "github.event.pull_request.head.repo.homepage"

    public val length: String = "github.event.pull_request.head.repo.size"

    public val stargazers_count: String = "github.event.pull_request.head.repo.stargazers_count"

    public val watchers_count: String = "github.event.pull_request.head.repo.watchers_count"

    public val language: String = "github.event.pull_request.head.repo.language"

    public val has_issues: String = "github.event.pull_request.head.repo.has_issues"

    public val has_projects: String = "github.event.pull_request.head.repo.has_projects"

    public val has_downloads: String = "github.event.pull_request.head.repo.has_downloads"

    public val has_wiki: String = "github.event.pull_request.head.repo.has_wiki"

    public val has_pages: String = "github.event.pull_request.head.repo.has_pages"

    public val forks_count: String = "github.event.pull_request.head.repo.forks_count"

    public val mirror_url: String = "github.event.pull_request.head.repo.mirror_url"

    public val archived: String = "github.event.pull_request.head.repo.archived"

    public val disabled: String = "github.event.pull_request.head.repo.disabled"

    public val open_issues_count: String = "github.event.pull_request.head.repo.open_issues_count"

    public val license: String = "github.event.pull_request.head.repo.license"

    public val forks: String = "github.event.pull_request.head.repo.forks"

    public val open_issues: String = "github.event.pull_request.head.repo.open_issues"

    public val watchers: String = "github.event.pull_request.head.repo.watchers"

    public val default_branch: String = "github.event.pull_request.head.repo.default_branch"

    public val allow_squash_merge: String = "github.event.pull_request.head.repo.allow_squash_merge"

    public val allow_merge_commit: String = "github.event.pull_request.head.repo.allow_merge_commit"

    public val allow_rebase_merge: String = "github.event.pull_request.head.repo.allow_rebase_merge"

    public val delete_branch_on_merge: String =
        "github.event.pull_request.head.repo.delete_branch_on_merge"
}

public object PullRequestEventContextPullRequestHead :
    ExpressionContext("github.event.pull_request.head") {
    public val label: String = "github.event.pull_request.head.label"

    public val ref: String = "github.event.pull_request.head.ref"

    public val sha: String = "github.event.pull_request.head.sha"

    public val user: PullRequestEventContextPullRequestHeadUser =
        PullRequestEventContextPullRequestHeadUser

    public val repo: PullRequestEventContextPullRequestHeadRepo =
        PullRequestEventContextPullRequestHeadRepo
}

public object PullRequestEventContextPullRequestBaseUser :
    ExpressionContext("github.event.pull_request.base.user") {
    public val login: String = "github.event.pull_request.base.user.login"

    public val id: String = "github.event.pull_request.base.user.id"

    public val node_id: String = "github.event.pull_request.base.user.node_id"

    public val avatar_url: String = "github.event.pull_request.base.user.avatar_url"

    public val gravatar_id: String = "github.event.pull_request.base.user.gravatar_id"

    public val url: String = "github.event.pull_request.base.user.url"

    public val html_url: String = "github.event.pull_request.base.user.html_url"

    public val followers_url: String = "github.event.pull_request.base.user.followers_url"

    public val following_url: String = "github.event.pull_request.base.user.following_url"

    public val gists_url: String = "github.event.pull_request.base.user.gists_url"

    public val starred_url: String = "github.event.pull_request.base.user.starred_url"

    public val subscriptions_url: String = "github.event.pull_request.base.user.subscriptions_url"

    public val organizations_url: String = "github.event.pull_request.base.user.organizations_url"

    public val repos_url: String = "github.event.pull_request.base.user.repos_url"

    public val events_url: String = "github.event.pull_request.base.user.events_url"

    public val received_events_url: String = "github.event.pull_request.base.user.received_events_url"

    public val type: String = "github.event.pull_request.base.user.type"

    public val site_admin: String = "github.event.pull_request.base.user.site_admin"
}

public object PullRequestEventContextPullRequestBaseRepoOwner :
    ExpressionContext("github.event.pull_request.base.repo.owner") {
    public val login: String = "github.event.pull_request.base.repo.owner.login"

    public val id: String = "github.event.pull_request.base.repo.owner.id"

    public val node_id: String = "github.event.pull_request.base.repo.owner.node_id"

    public val avatar_url: String = "github.event.pull_request.base.repo.owner.avatar_url"

    public val gravatar_id: String = "github.event.pull_request.base.repo.owner.gravatar_id"

    public val url: String = "github.event.pull_request.base.repo.owner.url"

    public val html_url: String = "github.event.pull_request.base.repo.owner.html_url"

    public val followers_url: String = "github.event.pull_request.base.repo.owner.followers_url"

    public val following_url: String = "github.event.pull_request.base.repo.owner.following_url"

    public val gists_url: String = "github.event.pull_request.base.repo.owner.gists_url"

    public val starred_url: String = "github.event.pull_request.base.repo.owner.starred_url"

    public val subscriptions_url: String =
        "github.event.pull_request.base.repo.owner.subscriptions_url"

    public val organizations_url: String =
        "github.event.pull_request.base.repo.owner.organizations_url"

    public val repos_url: String = "github.event.pull_request.base.repo.owner.repos_url"

    public val events_url: String = "github.event.pull_request.base.repo.owner.events_url"

    public val received_events_url: String =
        "github.event.pull_request.base.repo.owner.received_events_url"

    public val type: String = "github.event.pull_request.base.repo.owner.type"

    public val site_admin: String = "github.event.pull_request.base.repo.owner.site_admin"
}

public object PullRequestEventContextPullRequestBaseRepo :
    ExpressionContext("github.event.pull_request.base.repo") {
    public val id: String = "github.event.pull_request.base.repo.id"

    public val node_id: String = "github.event.pull_request.base.repo.node_id"

    public val name: String = "github.event.pull_request.base.repo.name"

    public val full_name: String = "github.event.pull_request.base.repo.full_name"

    public val `private`: String = "github.event.pull_request.base.repo.private"

    public val owner: PullRequestEventContextPullRequestBaseRepoOwner =
        PullRequestEventContextPullRequestBaseRepoOwner

    public val html_url: String = "github.event.pull_request.base.repo.html_url"

    public val description: String = "github.event.pull_request.base.repo.description"

    public val fork: String = "github.event.pull_request.base.repo.fork"

    public val url: String = "github.event.pull_request.base.repo.url"

    public val forks_url: String = "github.event.pull_request.base.repo.forks_url"

    public val keys_url: String = "github.event.pull_request.base.repo.keys_url"

    public val collaborators_url: String = "github.event.pull_request.base.repo.collaborators_url"

    public val teams_url: String = "github.event.pull_request.base.repo.teams_url"

    public val hooks_url: String = "github.event.pull_request.base.repo.hooks_url"

    public val issue_events_url: String = "github.event.pull_request.base.repo.issue_events_url"

    public val events_url: String = "github.event.pull_request.base.repo.events_url"

    public val assignees_url: String = "github.event.pull_request.base.repo.assignees_url"

    public val branches_url: String = "github.event.pull_request.base.repo.branches_url"

    public val tags_url: String = "github.event.pull_request.base.repo.tags_url"

    public val blobs_url: String = "github.event.pull_request.base.repo.blobs_url"

    public val git_tags_url: String = "github.event.pull_request.base.repo.git_tags_url"

    public val git_refs_url: String = "github.event.pull_request.base.repo.git_refs_url"

    public val trees_url: String = "github.event.pull_request.base.repo.trees_url"

    public val statuses_url: String = "github.event.pull_request.base.repo.statuses_url"

    public val languages_url: String = "github.event.pull_request.base.repo.languages_url"

    public val stargazers_url: String = "github.event.pull_request.base.repo.stargazers_url"

    public val contributors_url: String = "github.event.pull_request.base.repo.contributors_url"

    public val subscribers_url: String = "github.event.pull_request.base.repo.subscribers_url"

    public val subscription_url: String = "github.event.pull_request.base.repo.subscription_url"

    public val commits_url: String = "github.event.pull_request.base.repo.commits_url"

    public val git_commits_url: String = "github.event.pull_request.base.repo.git_commits_url"

    public val comments_url: String = "github.event.pull_request.base.repo.comments_url"

    public val issue_comment_url: String = "github.event.pull_request.base.repo.issue_comment_url"

    public val contents_url: String = "github.event.pull_request.base.repo.contents_url"

    public val compare_url: String = "github.event.pull_request.base.repo.compare_url"

    public val merges_url: String = "github.event.pull_request.base.repo.merges_url"

    public val archive_url: String = "github.event.pull_request.base.repo.archive_url"

    public val downloads_url: String = "github.event.pull_request.base.repo.downloads_url"

    public val issues_url: String = "github.event.pull_request.base.repo.issues_url"

    public val pulls_url: String = "github.event.pull_request.base.repo.pulls_url"

    public val milestones_url: String = "github.event.pull_request.base.repo.milestones_url"

    public val notifications_url: String = "github.event.pull_request.base.repo.notifications_url"

    public val labels_url: String = "github.event.pull_request.base.repo.labels_url"

    public val releases_url: String = "github.event.pull_request.base.repo.releases_url"

    public val deployments_url: String = "github.event.pull_request.base.repo.deployments_url"

    public val created_at: String = "github.event.pull_request.base.repo.created_at"

    public val updated_at: String = "github.event.pull_request.base.repo.updated_at"

    public val pushed_at: String = "github.event.pull_request.base.repo.pushed_at"

    public val git_url: String = "github.event.pull_request.base.repo.git_url"

    public val ssh_url: String = "github.event.pull_request.base.repo.ssh_url"

    public val clone_url: String = "github.event.pull_request.base.repo.clone_url"

    public val svn_url: String = "github.event.pull_request.base.repo.svn_url"

    public val homepage: String = "github.event.pull_request.base.repo.homepage"

    public val length: String = "github.event.pull_request.base.repo.size"

    public val stargazers_count: String = "github.event.pull_request.base.repo.stargazers_count"

    public val watchers_count: String = "github.event.pull_request.base.repo.watchers_count"

    public val language: String = "github.event.pull_request.base.repo.language"

    public val has_issues: String = "github.event.pull_request.base.repo.has_issues"

    public val has_projects: String = "github.event.pull_request.base.repo.has_projects"

    public val has_downloads: String = "github.event.pull_request.base.repo.has_downloads"

    public val has_wiki: String = "github.event.pull_request.base.repo.has_wiki"

    public val has_pages: String = "github.event.pull_request.base.repo.has_pages"

    public val forks_count: String = "github.event.pull_request.base.repo.forks_count"

    public val mirror_url: String = "github.event.pull_request.base.repo.mirror_url"

    public val archived: String = "github.event.pull_request.base.repo.archived"

    public val disabled: String = "github.event.pull_request.base.repo.disabled"

    public val open_issues_count: String = "github.event.pull_request.base.repo.open_issues_count"

    public val license: String = "github.event.pull_request.base.repo.license"

    public val forks: String = "github.event.pull_request.base.repo.forks"

    public val open_issues: String = "github.event.pull_request.base.repo.open_issues"

    public val watchers: String = "github.event.pull_request.base.repo.watchers"

    public val default_branch: String = "github.event.pull_request.base.repo.default_branch"

    public val allow_squash_merge: String = "github.event.pull_request.base.repo.allow_squash_merge"

    public val allow_merge_commit: String = "github.event.pull_request.base.repo.allow_merge_commit"

    public val allow_rebase_merge: String = "github.event.pull_request.base.repo.allow_rebase_merge"

    public val delete_branch_on_merge: String =
        "github.event.pull_request.base.repo.delete_branch_on_merge"
}

public object PullRequestEventContextPullRequestBase :
    ExpressionContext("github.event.pull_request.base") {
    public val label: String = "github.event.pull_request.base.label"

    public val ref: String = "github.event.pull_request.base.ref"

    public val sha: String = "github.event.pull_request.base.sha"

    public val user: PullRequestEventContextPullRequestBaseUser =
        PullRequestEventContextPullRequestBaseUser

    public val repo: PullRequestEventContextPullRequestBaseRepo =
        PullRequestEventContextPullRequestBaseRepo
}

public object PullRequestEventContextPullRequestLinksSelf :
    ExpressionContext("github.event.pull_request._links.self") {
    public val href: String = "github.event.pull_request._links.self.href"
}

public object PullRequestEventContextPullRequestLinksHtml :
    ExpressionContext("github.event.pull_request._links.html") {
    public val href: String = "github.event.pull_request._links.html.href"
}

public object PullRequestEventContextPullRequestLinksIssue :
    ExpressionContext("github.event.pull_request._links.issue") {
    public val href: String = "github.event.pull_request._links.issue.href"
}

public object PullRequestEventContextPullRequestLinksComments :
    ExpressionContext("github.event.pull_request._links.comments") {
    public val href: String = "github.event.pull_request._links.comments.href"
}

public object PullRequestEventContextPullRequestLinksReviewComments :
    ExpressionContext("github.event.pull_request._links.review_comments") {
    public val href: String = "github.event.pull_request._links.review_comments.href"
}

public object PullRequestEventContextPullRequestLinksReviewComment :
    ExpressionContext("github.event.pull_request._links.review_comment") {
    public val href: String = "github.event.pull_request._links.review_comment.href"
}

public object PullRequestEventContextPullRequestLinksCommits :
    ExpressionContext("github.event.pull_request._links.commits") {
    public val href: String = "github.event.pull_request._links.commits.href"
}

public object PullRequestEventContextPullRequestLinksStatuses :
    ExpressionContext("github.event.pull_request._links.statuses") {
    public val href: String = "github.event.pull_request._links.statuses.href"
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
    public val url: String = "github.event.pull_request.url"

    public val id: String = "github.event.pull_request.id"

    public val node_id: String = "github.event.pull_request.node_id"

    public val html_url: String = "github.event.pull_request.html_url"

    public val diff_url: String = "github.event.pull_request.diff_url"

    public val patch_url: String = "github.event.pull_request.patch_url"

    public val issue_url: String = "github.event.pull_request.issue_url"

    public val number: String = "github.event.pull_request.number"

    public val state: String = "github.event.pull_request.state"

    public val locked: String = "github.event.pull_request.locked"

    public val title: String = "github.event.pull_request.title"

    public val user: PullRequestEventContextPullRequestUser = PullRequestEventContextPullRequestUser

    public val body: String = "github.event.pull_request.body"

    public val created_at: String = "github.event.pull_request.created_at"

    public val updated_at: String = "github.event.pull_request.updated_at"

    public val closed_at: String = "github.event.pull_request.closed_at"

    public val merged_at: String = "github.event.pull_request.merged_at"

    public val merge_commit_sha: String = "github.event.pull_request.merge_commit_sha"

    public val assignee: String = "github.event.pull_request.assignee"

    public val assignees: List<String> = FakeList("github.event.pull_request.assignees")

    public val requested_reviewers: List<String> =
        FakeList("github.event.pull_request.requested_reviewers")

    public val requested_teams: List<String> = FakeList("github.event.pull_request.requested_teams")

    public val labels: List<String> = FakeList("github.event.pull_request.labels")

    public val milestone: String = "github.event.pull_request.milestone"

    public val commits_url: String = "github.event.pull_request.commits_url"

    public val review_comments_url: String = "github.event.pull_request.review_comments_url"

    public val review_comment_url: String = "github.event.pull_request.review_comment_url"

    public val comments_url: String = "github.event.pull_request.comments_url"

    public val statuses_url: String = "github.event.pull_request.statuses_url"

    public val head: PullRequestEventContextPullRequestHead = PullRequestEventContextPullRequestHead

    public val base: PullRequestEventContextPullRequestBase = PullRequestEventContextPullRequestBase

    public val _links: PullRequestEventContextPullRequestLinks =
        PullRequestEventContextPullRequestLinks

    public val author_association: String = "github.event.pull_request.author_association"

    public val draft: String = "github.event.pull_request.draft"

    public val merged: String = "github.event.pull_request.merged"

    public val mergeable: String = "github.event.pull_request.mergeable"

    public val rebaseable: String = "github.event.pull_request.rebaseable"

    public val mergeable_state: String = "github.event.pull_request.mergeable_state"

    public val merged_by: String = "github.event.pull_request.merged_by"

    public val comments: String = "github.event.pull_request.comments"

    public val review_comments: String = "github.event.pull_request.review_comments"

    public val maintainer_can_modify: String = "github.event.pull_request.maintainer_can_modify"

    public val commits: String = "github.event.pull_request.commits"

    public val additions: String = "github.event.pull_request.additions"

    public val deletions: String = "github.event.pull_request.deletions"

    public val changed_files: String = "github.event.pull_request.changed_files"
}

public object PullRequestEventContextRepositoryOwner :
    ExpressionContext("github.event.repository.owner") {
    public val login: String = "github.event.repository.owner.login"

    public val id: String = "github.event.repository.owner.id"

    public val node_id: String = "github.event.repository.owner.node_id"

    public val avatar_url: String = "github.event.repository.owner.avatar_url"

    public val gravatar_id: String = "github.event.repository.owner.gravatar_id"

    public val url: String = "github.event.repository.owner.url"

    public val html_url: String = "github.event.repository.owner.html_url"

    public val followers_url: String = "github.event.repository.owner.followers_url"

    public val following_url: String = "github.event.repository.owner.following_url"

    public val gists_url: String = "github.event.repository.owner.gists_url"

    public val starred_url: String = "github.event.repository.owner.starred_url"

    public val subscriptions_url: String = "github.event.repository.owner.subscriptions_url"

    public val organizations_url: String = "github.event.repository.owner.organizations_url"

    public val repos_url: String = "github.event.repository.owner.repos_url"

    public val events_url: String = "github.event.repository.owner.events_url"

    public val received_events_url: String = "github.event.repository.owner.received_events_url"

    public val type: String = "github.event.repository.owner.type"

    public val site_admin: String = "github.event.repository.owner.site_admin"
}

public object PullRequestEventContextRepository : ExpressionContext("github.event.repository") {
    public val id: String = "github.event.repository.id"

    public val node_id: String = "github.event.repository.node_id"

    public val name: String = "github.event.repository.name"

    public val full_name: String = "github.event.repository.full_name"

    public val `private`: String = "github.event.repository.private"

    public val owner: PullRequestEventContextRepositoryOwner = PullRequestEventContextRepositoryOwner

    public val html_url: String = "github.event.repository.html_url"

    public val description: String = "github.event.repository.description"

    public val fork: String = "github.event.repository.fork"

    public val url: String = "github.event.repository.url"

    public val forks_url: String = "github.event.repository.forks_url"

    public val keys_url: String = "github.event.repository.keys_url"

    public val collaborators_url: String = "github.event.repository.collaborators_url"

    public val teams_url: String = "github.event.repository.teams_url"

    public val hooks_url: String = "github.event.repository.hooks_url"

    public val issue_events_url: String = "github.event.repository.issue_events_url"

    public val events_url: String = "github.event.repository.events_url"

    public val assignees_url: String = "github.event.repository.assignees_url"

    public val branches_url: String = "github.event.repository.branches_url"

    public val tags_url: String = "github.event.repository.tags_url"

    public val blobs_url: String = "github.event.repository.blobs_url"

    public val git_tags_url: String = "github.event.repository.git_tags_url"

    public val git_refs_url: String = "github.event.repository.git_refs_url"

    public val trees_url: String = "github.event.repository.trees_url"

    public val statuses_url: String = "github.event.repository.statuses_url"

    public val languages_url: String = "github.event.repository.languages_url"

    public val stargazers_url: String = "github.event.repository.stargazers_url"

    public val contributors_url: String = "github.event.repository.contributors_url"

    public val subscribers_url: String = "github.event.repository.subscribers_url"

    public val subscription_url: String = "github.event.repository.subscription_url"

    public val commits_url: String = "github.event.repository.commits_url"

    public val git_commits_url: String = "github.event.repository.git_commits_url"

    public val comments_url: String = "github.event.repository.comments_url"

    public val issue_comment_url: String = "github.event.repository.issue_comment_url"

    public val contents_url: String = "github.event.repository.contents_url"

    public val compare_url: String = "github.event.repository.compare_url"

    public val merges_url: String = "github.event.repository.merges_url"

    public val archive_url: String = "github.event.repository.archive_url"

    public val downloads_url: String = "github.event.repository.downloads_url"

    public val issues_url: String = "github.event.repository.issues_url"

    public val pulls_url: String = "github.event.repository.pulls_url"

    public val milestones_url: String = "github.event.repository.milestones_url"

    public val notifications_url: String = "github.event.repository.notifications_url"

    public val labels_url: String = "github.event.repository.labels_url"

    public val releases_url: String = "github.event.repository.releases_url"

    public val deployments_url: String = "github.event.repository.deployments_url"

    public val created_at: String = "github.event.repository.created_at"

    public val updated_at: String = "github.event.repository.updated_at"

    public val pushed_at: String = "github.event.repository.pushed_at"

    public val git_url: String = "github.event.repository.git_url"

    public val ssh_url: String = "github.event.repository.ssh_url"

    public val clone_url: String = "github.event.repository.clone_url"

    public val svn_url: String = "github.event.repository.svn_url"

    public val homepage: String = "github.event.repository.homepage"

    public val length: String = "github.event.repository.size"

    public val stargazers_count: String = "github.event.repository.stargazers_count"

    public val watchers_count: String = "github.event.repository.watchers_count"

    public val language: String = "github.event.repository.language"

    public val has_issues: String = "github.event.repository.has_issues"

    public val has_projects: String = "github.event.repository.has_projects"

    public val has_downloads: String = "github.event.repository.has_downloads"

    public val has_wiki: String = "github.event.repository.has_wiki"

    public val has_pages: String = "github.event.repository.has_pages"

    public val forks_count: String = "github.event.repository.forks_count"

    public val mirror_url: String = "github.event.repository.mirror_url"

    public val archived: String = "github.event.repository.archived"

    public val disabled: String = "github.event.repository.disabled"

    public val open_issues_count: String = "github.event.repository.open_issues_count"

    public val license: String = "github.event.repository.license"

    public val forks: String = "github.event.repository.forks"

    public val open_issues: String = "github.event.repository.open_issues"

    public val watchers: String = "github.event.repository.watchers"

    public val default_branch: String = "github.event.repository.default_branch"
}

public object PullRequestEventContextSender : ExpressionContext("github.event.sender") {
    public val login: String = "github.event.sender.login"

    public val id: String = "github.event.sender.id"

    public val node_id: String = "github.event.sender.node_id"

    public val avatar_url: String = "github.event.sender.avatar_url"

    public val gravatar_id: String = "github.event.sender.gravatar_id"

    public val url: String = "github.event.sender.url"

    public val html_url: String = "github.event.sender.html_url"

    public val followers_url: String = "github.event.sender.followers_url"

    public val following_url: String = "github.event.sender.following_url"

    public val gists_url: String = "github.event.sender.gists_url"

    public val starred_url: String = "github.event.sender.starred_url"

    public val subscriptions_url: String = "github.event.sender.subscriptions_url"

    public val organizations_url: String = "github.event.sender.organizations_url"

    public val repos_url: String = "github.event.sender.repos_url"

    public val events_url: String = "github.event.sender.events_url"

    public val received_events_url: String = "github.event.sender.received_events_url"

    public val type: String = "github.event.sender.type"

    public val site_admin: String = "github.event.sender.site_admin"
}

public object PullRequestEventContext : ExpressionContext("github.event") {
    public val action: String = "github.event.action"

    public val number: String = "github.event.number"

    public val pull_request: PullRequestEventContextPullRequest = PullRequestEventContextPullRequest

    public val repository: PullRequestEventContextRepository = PullRequestEventContextRepository

    public val sender: PullRequestEventContextSender = PullRequestEventContextSender
}
