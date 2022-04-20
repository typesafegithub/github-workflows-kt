package it.krzeminski.githubactions.wrappergenerator.payload

import kotlin.String

public val event: Event = Event

public object EventPullRequestUser {
    public val login: String = "event.pull_request.user.login"

    public val id: String = "event.pull_request.user.id"

    public val nodeId: String = "event.pull_request.user.node_id"

    public val avatarUrl: String = "event.pull_request.user.avatar_url"

    public val gravatarId: String = "event.pull_request.user.gravatar_id"

    public val url: String = "event.pull_request.user.url"

    public val htmlUrl: String = "event.pull_request.user.html_url"

    public val followersUrl: String = "event.pull_request.user.followers_url"

    public val followingUrl: String = "event.pull_request.user.following_url"

    public val gistsUrl: String = "event.pull_request.user.gists_url"

    public val starredUrl: String = "event.pull_request.user.starred_url"

    public val subscriptionsUrl: String = "event.pull_request.user.subscriptions_url"

    public val organizationsUrl: String = "event.pull_request.user.organizations_url"

    public val reposUrl: String = "event.pull_request.user.repos_url"

    public val eventsUrl: String = "event.pull_request.user.events_url"

    public val receivedEventsUrl: String = "event.pull_request.user.received_events_url"

    public val type: String = "event.pull_request.user.type"

    public val siteAdmin: String = "event.pull_request.user.site_admin"
}

public object EventPullRequestHeadUser {
    public val login: String = "event.pull_request.head.user.login"

    public val id: String = "event.pull_request.head.user.id"

    public val nodeId: String = "event.pull_request.head.user.node_id"

    public val avatarUrl: String = "event.pull_request.head.user.avatar_url"

    public val gravatarId: String = "event.pull_request.head.user.gravatar_id"

    public val url: String = "event.pull_request.head.user.url"

    public val htmlUrl: String = "event.pull_request.head.user.html_url"

    public val followersUrl: String = "event.pull_request.head.user.followers_url"

    public val followingUrl: String = "event.pull_request.head.user.following_url"

    public val gistsUrl: String = "event.pull_request.head.user.gists_url"

    public val starredUrl: String = "event.pull_request.head.user.starred_url"

    public val subscriptionsUrl: String = "event.pull_request.head.user.subscriptions_url"

    public val organizationsUrl: String = "event.pull_request.head.user.organizations_url"

    public val reposUrl: String = "event.pull_request.head.user.repos_url"

    public val eventsUrl: String = "event.pull_request.head.user.events_url"

    public val receivedEventsUrl: String = "event.pull_request.head.user.received_events_url"

    public val type: String = "event.pull_request.head.user.type"

    public val siteAdmin: String = "event.pull_request.head.user.site_admin"
}

public object EventPullRequestHeadRepoOwner {
    public val login: String = "event.pull_request.head.repo.owner.login"

    public val id: String = "event.pull_request.head.repo.owner.id"

    public val nodeId: String = "event.pull_request.head.repo.owner.node_id"

    public val avatarUrl: String = "event.pull_request.head.repo.owner.avatar_url"

    public val gravatarId: String = "event.pull_request.head.repo.owner.gravatar_id"

    public val url: String = "event.pull_request.head.repo.owner.url"

    public val htmlUrl: String = "event.pull_request.head.repo.owner.html_url"

    public val followersUrl: String = "event.pull_request.head.repo.owner.followers_url"

    public val followingUrl: String = "event.pull_request.head.repo.owner.following_url"

    public val gistsUrl: String = "event.pull_request.head.repo.owner.gists_url"

    public val starredUrl: String = "event.pull_request.head.repo.owner.starred_url"

    public val subscriptionsUrl: String = "event.pull_request.head.repo.owner.subscriptions_url"

    public val organizationsUrl: String = "event.pull_request.head.repo.owner.organizations_url"

    public val reposUrl: String = "event.pull_request.head.repo.owner.repos_url"

    public val eventsUrl: String = "event.pull_request.head.repo.owner.events_url"

    public val receivedEventsUrl: String = "event.pull_request.head.repo.owner.received_events_url"

    public val type: String = "event.pull_request.head.repo.owner.type"

    public val siteAdmin: String = "event.pull_request.head.repo.owner.site_admin"
}

public object EventPullRequestHeadRepo {
    public val id: String = "event.pull_request.head.repo.id"

    public val nodeId: String = "event.pull_request.head.repo.node_id"

    public val name: String = "event.pull_request.head.repo.name"

    public val fullName: String = "event.pull_request.head.repo.full_name"

    public val `private`: String = "event.pull_request.head.repo.private"

    public val owner: EventPullRequestHeadRepoOwner = EventPullRequestHeadRepoOwner

    public val htmlUrl: String = "event.pull_request.head.repo.html_url"

    public val description: String = "event.pull_request.head.repo.description"

    public val fork: String = "event.pull_request.head.repo.fork"

    public val url: String = "event.pull_request.head.repo.url"

    public val forksUrl: String = "event.pull_request.head.repo.forks_url"

    public val keysUrl: String = "event.pull_request.head.repo.keys_url"

    public val collaboratorsUrl: String = "event.pull_request.head.repo.collaborators_url"

    public val teamsUrl: String = "event.pull_request.head.repo.teams_url"

    public val hooksUrl: String = "event.pull_request.head.repo.hooks_url"

    public val issueEventsUrl: String = "event.pull_request.head.repo.issue_events_url"

    public val eventsUrl: String = "event.pull_request.head.repo.events_url"

    public val assigneesUrl: String = "event.pull_request.head.repo.assignees_url"

    public val branchesUrl: String = "event.pull_request.head.repo.branches_url"

    public val tagsUrl: String = "event.pull_request.head.repo.tags_url"

    public val blobsUrl: String = "event.pull_request.head.repo.blobs_url"

    public val gitTagsUrl: String = "event.pull_request.head.repo.git_tags_url"

    public val gitRefsUrl: String = "event.pull_request.head.repo.git_refs_url"

    public val treesUrl: String = "event.pull_request.head.repo.trees_url"

    public val statusesUrl: String = "event.pull_request.head.repo.statuses_url"

    public val languagesUrl: String = "event.pull_request.head.repo.languages_url"

    public val stargazersUrl: String = "event.pull_request.head.repo.stargazers_url"

    public val contributorsUrl: String = "event.pull_request.head.repo.contributors_url"

    public val subscribersUrl: String = "event.pull_request.head.repo.subscribers_url"

    public val subscriptionUrl: String = "event.pull_request.head.repo.subscription_url"

    public val commitsUrl: String = "event.pull_request.head.repo.commits_url"

    public val gitCommitsUrl: String = "event.pull_request.head.repo.git_commits_url"

    public val commentsUrl: String = "event.pull_request.head.repo.comments_url"

    public val issueCommentUrl: String = "event.pull_request.head.repo.issue_comment_url"

    public val contentsUrl: String = "event.pull_request.head.repo.contents_url"

    public val compareUrl: String = "event.pull_request.head.repo.compare_url"

    public val mergesUrl: String = "event.pull_request.head.repo.merges_url"

    public val archiveUrl: String = "event.pull_request.head.repo.archive_url"

    public val downloadsUrl: String = "event.pull_request.head.repo.downloads_url"

    public val issuesUrl: String = "event.pull_request.head.repo.issues_url"

    public val pullsUrl: String = "event.pull_request.head.repo.pulls_url"

    public val milestonesUrl: String = "event.pull_request.head.repo.milestones_url"

    public val notificationsUrl: String = "event.pull_request.head.repo.notifications_url"

    public val labelsUrl: String = "event.pull_request.head.repo.labels_url"

    public val releasesUrl: String = "event.pull_request.head.repo.releases_url"

    public val deploymentsUrl: String = "event.pull_request.head.repo.deployments_url"

    public val createdAt: String = "event.pull_request.head.repo.created_at"

    public val updatedAt: String = "event.pull_request.head.repo.updated_at"

    public val pushedAt: String = "event.pull_request.head.repo.pushed_at"

    public val gitUrl: String = "event.pull_request.head.repo.git_url"

    public val sshUrl: String = "event.pull_request.head.repo.ssh_url"

    public val cloneUrl: String = "event.pull_request.head.repo.clone_url"

    public val svnUrl: String = "event.pull_request.head.repo.svn_url"

    public val homepage: String = "event.pull_request.head.repo.homepage"

    public val size: String = "event.pull_request.head.repo.size"

    public val stargazersCount: String = "event.pull_request.head.repo.stargazers_count"

    public val watchersCount: String = "event.pull_request.head.repo.watchers_count"

    public val language: String = "event.pull_request.head.repo.language"

    public val hasIssues: String = "event.pull_request.head.repo.has_issues"

    public val hasProjects: String = "event.pull_request.head.repo.has_projects"

    public val hasDownloads: String = "event.pull_request.head.repo.has_downloads"

    public val hasWiki: String = "event.pull_request.head.repo.has_wiki"

    public val hasPages: String = "event.pull_request.head.repo.has_pages"

    public val forksCount: String = "event.pull_request.head.repo.forks_count"

    public val mirrorUrl: String = "event.pull_request.head.repo.mirror_url"

    public val archived: String = "event.pull_request.head.repo.archived"

    public val disabled: String = "event.pull_request.head.repo.disabled"

    public val openIssuesCount: String = "event.pull_request.head.repo.open_issues_count"

    public val license: String = "event.pull_request.head.repo.license"

    public val forks: String = "event.pull_request.head.repo.forks"

    public val openIssues: String = "event.pull_request.head.repo.open_issues"

    public val watchers: String = "event.pull_request.head.repo.watchers"

    public val defaultBranch: String = "event.pull_request.head.repo.default_branch"

    public val allowSquashMerge: String = "event.pull_request.head.repo.allow_squash_merge"

    public val allowMergeCommit: String = "event.pull_request.head.repo.allow_merge_commit"

    public val allowRebaseMerge: String = "event.pull_request.head.repo.allow_rebase_merge"

    public val deleteBranchOnMerge: String = "event.pull_request.head.repo.delete_branch_on_merge"
}

public object EventPullRequestHead {
    public val label: String = "event.pull_request.head.label"

    public val ref: String = "event.pull_request.head.ref"

    public val sha: String = "event.pull_request.head.sha"

    public val user: EventPullRequestHeadUser = EventPullRequestHeadUser

    public val repo: EventPullRequestHeadRepo = EventPullRequestHeadRepo
}

public object EventPullRequestBaseUser {
    public val login: String = "event.pull_request.base.user.login"

    public val id: String = "event.pull_request.base.user.id"

    public val nodeId: String = "event.pull_request.base.user.node_id"

    public val avatarUrl: String = "event.pull_request.base.user.avatar_url"

    public val gravatarId: String = "event.pull_request.base.user.gravatar_id"

    public val url: String = "event.pull_request.base.user.url"

    public val htmlUrl: String = "event.pull_request.base.user.html_url"

    public val followersUrl: String = "event.pull_request.base.user.followers_url"

    public val followingUrl: String = "event.pull_request.base.user.following_url"

    public val gistsUrl: String = "event.pull_request.base.user.gists_url"

    public val starredUrl: String = "event.pull_request.base.user.starred_url"

    public val subscriptionsUrl: String = "event.pull_request.base.user.subscriptions_url"

    public val organizationsUrl: String = "event.pull_request.base.user.organizations_url"

    public val reposUrl: String = "event.pull_request.base.user.repos_url"

    public val eventsUrl: String = "event.pull_request.base.user.events_url"

    public val receivedEventsUrl: String = "event.pull_request.base.user.received_events_url"

    public val type: String = "event.pull_request.base.user.type"

    public val siteAdmin: String = "event.pull_request.base.user.site_admin"
}

public object EventPullRequestBaseRepoOwner {
    public val login: String = "event.pull_request.base.repo.owner.login"

    public val id: String = "event.pull_request.base.repo.owner.id"

    public val nodeId: String = "event.pull_request.base.repo.owner.node_id"

    public val avatarUrl: String = "event.pull_request.base.repo.owner.avatar_url"

    public val gravatarId: String = "event.pull_request.base.repo.owner.gravatar_id"

    public val url: String = "event.pull_request.base.repo.owner.url"

    public val htmlUrl: String = "event.pull_request.base.repo.owner.html_url"

    public val followersUrl: String = "event.pull_request.base.repo.owner.followers_url"

    public val followingUrl: String = "event.pull_request.base.repo.owner.following_url"

    public val gistsUrl: String = "event.pull_request.base.repo.owner.gists_url"

    public val starredUrl: String = "event.pull_request.base.repo.owner.starred_url"

    public val subscriptionsUrl: String = "event.pull_request.base.repo.owner.subscriptions_url"

    public val organizationsUrl: String = "event.pull_request.base.repo.owner.organizations_url"

    public val reposUrl: String = "event.pull_request.base.repo.owner.repos_url"

    public val eventsUrl: String = "event.pull_request.base.repo.owner.events_url"

    public val receivedEventsUrl: String = "event.pull_request.base.repo.owner.received_events_url"

    public val type: String = "event.pull_request.base.repo.owner.type"

    public val siteAdmin: String = "event.pull_request.base.repo.owner.site_admin"
}

public object EventPullRequestBaseRepo {
    public val id: String = "event.pull_request.base.repo.id"

    public val nodeId: String = "event.pull_request.base.repo.node_id"

    public val name: String = "event.pull_request.base.repo.name"

    public val fullName: String = "event.pull_request.base.repo.full_name"

    public val `private`: String = "event.pull_request.base.repo.private"

    public val owner: EventPullRequestBaseRepoOwner = EventPullRequestBaseRepoOwner

    public val htmlUrl: String = "event.pull_request.base.repo.html_url"

    public val description: String = "event.pull_request.base.repo.description"

    public val fork: String = "event.pull_request.base.repo.fork"

    public val url: String = "event.pull_request.base.repo.url"

    public val forksUrl: String = "event.pull_request.base.repo.forks_url"

    public val keysUrl: String = "event.pull_request.base.repo.keys_url"

    public val collaboratorsUrl: String = "event.pull_request.base.repo.collaborators_url"

    public val teamsUrl: String = "event.pull_request.base.repo.teams_url"

    public val hooksUrl: String = "event.pull_request.base.repo.hooks_url"

    public val issueEventsUrl: String = "event.pull_request.base.repo.issue_events_url"

    public val eventsUrl: String = "event.pull_request.base.repo.events_url"

    public val assigneesUrl: String = "event.pull_request.base.repo.assignees_url"

    public val branchesUrl: String = "event.pull_request.base.repo.branches_url"

    public val tagsUrl: String = "event.pull_request.base.repo.tags_url"

    public val blobsUrl: String = "event.pull_request.base.repo.blobs_url"

    public val gitTagsUrl: String = "event.pull_request.base.repo.git_tags_url"

    public val gitRefsUrl: String = "event.pull_request.base.repo.git_refs_url"

    public val treesUrl: String = "event.pull_request.base.repo.trees_url"

    public val statusesUrl: String = "event.pull_request.base.repo.statuses_url"

    public val languagesUrl: String = "event.pull_request.base.repo.languages_url"

    public val stargazersUrl: String = "event.pull_request.base.repo.stargazers_url"

    public val contributorsUrl: String = "event.pull_request.base.repo.contributors_url"

    public val subscribersUrl: String = "event.pull_request.base.repo.subscribers_url"

    public val subscriptionUrl: String = "event.pull_request.base.repo.subscription_url"

    public val commitsUrl: String = "event.pull_request.base.repo.commits_url"

    public val gitCommitsUrl: String = "event.pull_request.base.repo.git_commits_url"

    public val commentsUrl: String = "event.pull_request.base.repo.comments_url"

    public val issueCommentUrl: String = "event.pull_request.base.repo.issue_comment_url"

    public val contentsUrl: String = "event.pull_request.base.repo.contents_url"

    public val compareUrl: String = "event.pull_request.base.repo.compare_url"

    public val mergesUrl: String = "event.pull_request.base.repo.merges_url"

    public val archiveUrl: String = "event.pull_request.base.repo.archive_url"

    public val downloadsUrl: String = "event.pull_request.base.repo.downloads_url"

    public val issuesUrl: String = "event.pull_request.base.repo.issues_url"

    public val pullsUrl: String = "event.pull_request.base.repo.pulls_url"

    public val milestonesUrl: String = "event.pull_request.base.repo.milestones_url"

    public val notificationsUrl: String = "event.pull_request.base.repo.notifications_url"

    public val labelsUrl: String = "event.pull_request.base.repo.labels_url"

    public val releasesUrl: String = "event.pull_request.base.repo.releases_url"

    public val deploymentsUrl: String = "event.pull_request.base.repo.deployments_url"

    public val createdAt: String = "event.pull_request.base.repo.created_at"

    public val updatedAt: String = "event.pull_request.base.repo.updated_at"

    public val pushedAt: String = "event.pull_request.base.repo.pushed_at"

    public val gitUrl: String = "event.pull_request.base.repo.git_url"

    public val sshUrl: String = "event.pull_request.base.repo.ssh_url"

    public val cloneUrl: String = "event.pull_request.base.repo.clone_url"

    public val svnUrl: String = "event.pull_request.base.repo.svn_url"

    public val homepage: String = "event.pull_request.base.repo.homepage"

    public val size: String = "event.pull_request.base.repo.size"

    public val stargazersCount: String = "event.pull_request.base.repo.stargazers_count"

    public val watchersCount: String = "event.pull_request.base.repo.watchers_count"

    public val language: String = "event.pull_request.base.repo.language"

    public val hasIssues: String = "event.pull_request.base.repo.has_issues"

    public val hasProjects: String = "event.pull_request.base.repo.has_projects"

    public val hasDownloads: String = "event.pull_request.base.repo.has_downloads"

    public val hasWiki: String = "event.pull_request.base.repo.has_wiki"

    public val hasPages: String = "event.pull_request.base.repo.has_pages"

    public val forksCount: String = "event.pull_request.base.repo.forks_count"

    public val mirrorUrl: String = "event.pull_request.base.repo.mirror_url"

    public val archived: String = "event.pull_request.base.repo.archived"

    public val disabled: String = "event.pull_request.base.repo.disabled"

    public val openIssuesCount: String = "event.pull_request.base.repo.open_issues_count"

    public val license: String = "event.pull_request.base.repo.license"

    public val forks: String = "event.pull_request.base.repo.forks"

    public val openIssues: String = "event.pull_request.base.repo.open_issues"

    public val watchers: String = "event.pull_request.base.repo.watchers"

    public val defaultBranch: String = "event.pull_request.base.repo.default_branch"

    public val allowSquashMerge: String = "event.pull_request.base.repo.allow_squash_merge"

    public val allowMergeCommit: String = "event.pull_request.base.repo.allow_merge_commit"

    public val allowRebaseMerge: String = "event.pull_request.base.repo.allow_rebase_merge"

    public val deleteBranchOnMerge: String = "event.pull_request.base.repo.delete_branch_on_merge"
}

public object EventPullRequestBase {
    public val label: String = "event.pull_request.base.label"

    public val ref: String = "event.pull_request.base.ref"

    public val sha: String = "event.pull_request.base.sha"

    public val user: EventPullRequestBaseUser = EventPullRequestBaseUser

    public val repo: EventPullRequestBaseRepo = EventPullRequestBaseRepo
}

public object EventPullRequestLinksSelf {
    public val href: String = "event.pull_request._links.self.href"
}

public object EventPullRequestLinksHtml {
    public val href: String = "event.pull_request._links.html.href"
}

public object EventPullRequestLinksIssue {
    public val href: String = "event.pull_request._links.issue.href"
}

public object EventPullRequestLinksComments {
    public val href: String = "event.pull_request._links.comments.href"
}

public object EventPullRequestLinksReviewComments {
    public val href: String = "event.pull_request._links.review_comments.href"
}

public object EventPullRequestLinksReviewComment {
    public val href: String = "event.pull_request._links.review_comment.href"
}

public object EventPullRequestLinksCommits {
    public val href: String = "event.pull_request._links.commits.href"
}

public object EventPullRequestLinksStatuses {
    public val href: String = "event.pull_request._links.statuses.href"
}

public object EventPullRequestLinks {
    public val self: EventPullRequestLinksSelf = EventPullRequestLinksSelf

    public val html: EventPullRequestLinksHtml = EventPullRequestLinksHtml

    public val issue: EventPullRequestLinksIssue = EventPullRequestLinksIssue

    public val comments: EventPullRequestLinksComments = EventPullRequestLinksComments

    public val reviewComments: EventPullRequestLinksReviewComments =
        EventPullRequestLinksReviewComments

    public val reviewComment: EventPullRequestLinksReviewComment = EventPullRequestLinksReviewComment

    public val commits: EventPullRequestLinksCommits = EventPullRequestLinksCommits

    public val statuses: EventPullRequestLinksStatuses = EventPullRequestLinksStatuses
}

public object EventPullRequest {
    public val url: String = "event.pull_request.url"

    public val id: String = "event.pull_request.id"

    public val nodeId: String = "event.pull_request.node_id"

    public val htmlUrl: String = "event.pull_request.html_url"

    public val diffUrl: String = "event.pull_request.diff_url"

    public val patchUrl: String = "event.pull_request.patch_url"

    public val issueUrl: String = "event.pull_request.issue_url"

    public val number: String = "event.pull_request.number"

    public val state: String = "event.pull_request.state"

    public val locked: String = "event.pull_request.locked"

    public val title: String = "event.pull_request.title"

    public val user: EventPullRequestUser = EventPullRequestUser

    public val body: String = "event.pull_request.body"

    public val createdAt: String = "event.pull_request.created_at"

    public val updatedAt: String = "event.pull_request.updated_at"

    public val closedAt: String = "event.pull_request.closed_at"

    public val mergedAt: String = "event.pull_request.merged_at"

    public val mergeCommitSha: String = "event.pull_request.merge_commit_sha"

    public val assignee: String = "event.pull_request.assignee"

    public val milestone: String = "event.pull_request.milestone"

    public val commitsUrl: String = "event.pull_request.commits_url"

    public val reviewCommentsUrl: String = "event.pull_request.review_comments_url"

    public val reviewCommentUrl: String = "event.pull_request.review_comment_url"

    public val commentsUrl: String = "event.pull_request.comments_url"

    public val statusesUrl: String = "event.pull_request.statuses_url"

    public val head: EventPullRequestHead = EventPullRequestHead

    public val base: EventPullRequestBase = EventPullRequestBase

    public val links: EventPullRequestLinks = EventPullRequestLinks

    public val authorAssociation: String = "event.pull_request.author_association"

    public val draft: String = "event.pull_request.draft"

    public val merged: String = "event.pull_request.merged"

    public val mergeable: String = "event.pull_request.mergeable"

    public val rebaseable: String = "event.pull_request.rebaseable"

    public val mergeableState: String = "event.pull_request.mergeable_state"

    public val mergedBy: String = "event.pull_request.merged_by"

    public val comments: String = "event.pull_request.comments"

    public val reviewComments: String = "event.pull_request.review_comments"

    public val maintainerCanModify: String = "event.pull_request.maintainer_can_modify"

    public val commits: String = "event.pull_request.commits"

    public val additions: String = "event.pull_request.additions"

    public val deletions: String = "event.pull_request.deletions"

    public val changedFiles: String = "event.pull_request.changed_files"
}

public object EventRepositoryOwner {
    public val login: String = "event.repository.owner.login"

    public val id: String = "event.repository.owner.id"

    public val nodeId: String = "event.repository.owner.node_id"

    public val avatarUrl: String = "event.repository.owner.avatar_url"

    public val gravatarId: String = "event.repository.owner.gravatar_id"

    public val url: String = "event.repository.owner.url"

    public val htmlUrl: String = "event.repository.owner.html_url"

    public val followersUrl: String = "event.repository.owner.followers_url"

    public val followingUrl: String = "event.repository.owner.following_url"

    public val gistsUrl: String = "event.repository.owner.gists_url"

    public val starredUrl: String = "event.repository.owner.starred_url"

    public val subscriptionsUrl: String = "event.repository.owner.subscriptions_url"

    public val organizationsUrl: String = "event.repository.owner.organizations_url"

    public val reposUrl: String = "event.repository.owner.repos_url"

    public val eventsUrl: String = "event.repository.owner.events_url"

    public val receivedEventsUrl: String = "event.repository.owner.received_events_url"

    public val type: String = "event.repository.owner.type"

    public val siteAdmin: String = "event.repository.owner.site_admin"
}

public object EventRepository {
    public val id: String = "event.repository.id"

    public val nodeId: String = "event.repository.node_id"

    public val name: String = "event.repository.name"

    public val fullName: String = "event.repository.full_name"

    public val `private`: String = "event.repository.private"

    public val owner: EventRepositoryOwner = EventRepositoryOwner

    public val htmlUrl: String = "event.repository.html_url"

    public val description: String = "event.repository.description"

    public val fork: String = "event.repository.fork"

    public val url: String = "event.repository.url"

    public val forksUrl: String = "event.repository.forks_url"

    public val keysUrl: String = "event.repository.keys_url"

    public val collaboratorsUrl: String = "event.repository.collaborators_url"

    public val teamsUrl: String = "event.repository.teams_url"

    public val hooksUrl: String = "event.repository.hooks_url"

    public val issueEventsUrl: String = "event.repository.issue_events_url"

    public val eventsUrl: String = "event.repository.events_url"

    public val assigneesUrl: String = "event.repository.assignees_url"

    public val branchesUrl: String = "event.repository.branches_url"

    public val tagsUrl: String = "event.repository.tags_url"

    public val blobsUrl: String = "event.repository.blobs_url"

    public val gitTagsUrl: String = "event.repository.git_tags_url"

    public val gitRefsUrl: String = "event.repository.git_refs_url"

    public val treesUrl: String = "event.repository.trees_url"

    public val statusesUrl: String = "event.repository.statuses_url"

    public val languagesUrl: String = "event.repository.languages_url"

    public val stargazersUrl: String = "event.repository.stargazers_url"

    public val contributorsUrl: String = "event.repository.contributors_url"

    public val subscribersUrl: String = "event.repository.subscribers_url"

    public val subscriptionUrl: String = "event.repository.subscription_url"

    public val commitsUrl: String = "event.repository.commits_url"

    public val gitCommitsUrl: String = "event.repository.git_commits_url"

    public val commentsUrl: String = "event.repository.comments_url"

    public val issueCommentUrl: String = "event.repository.issue_comment_url"

    public val contentsUrl: String = "event.repository.contents_url"

    public val compareUrl: String = "event.repository.compare_url"

    public val mergesUrl: String = "event.repository.merges_url"

    public val archiveUrl: String = "event.repository.archive_url"

    public val downloadsUrl: String = "event.repository.downloads_url"

    public val issuesUrl: String = "event.repository.issues_url"

    public val pullsUrl: String = "event.repository.pulls_url"

    public val milestonesUrl: String = "event.repository.milestones_url"

    public val notificationsUrl: String = "event.repository.notifications_url"

    public val labelsUrl: String = "event.repository.labels_url"

    public val releasesUrl: String = "event.repository.releases_url"

    public val deploymentsUrl: String = "event.repository.deployments_url"

    public val createdAt: String = "event.repository.created_at"

    public val updatedAt: String = "event.repository.updated_at"

    public val pushedAt: String = "event.repository.pushed_at"

    public val gitUrl: String = "event.repository.git_url"

    public val sshUrl: String = "event.repository.ssh_url"

    public val cloneUrl: String = "event.repository.clone_url"

    public val svnUrl: String = "event.repository.svn_url"

    public val homepage: String = "event.repository.homepage"

    public val size: String = "event.repository.size"

    public val stargazersCount: String = "event.repository.stargazers_count"

    public val watchersCount: String = "event.repository.watchers_count"

    public val language: String = "event.repository.language"

    public val hasIssues: String = "event.repository.has_issues"

    public val hasProjects: String = "event.repository.has_projects"

    public val hasDownloads: String = "event.repository.has_downloads"

    public val hasWiki: String = "event.repository.has_wiki"

    public val hasPages: String = "event.repository.has_pages"

    public val forksCount: String = "event.repository.forks_count"

    public val mirrorUrl: String = "event.repository.mirror_url"

    public val archived: String = "event.repository.archived"

    public val disabled: String = "event.repository.disabled"

    public val openIssuesCount: String = "event.repository.open_issues_count"

    public val license: String = "event.repository.license"

    public val forks: String = "event.repository.forks"

    public val openIssues: String = "event.repository.open_issues"

    public val watchers: String = "event.repository.watchers"

    public val defaultBranch: String = "event.repository.default_branch"
}

public object EventSender {
    public val login: String = "event.sender.login"

    public val id: String = "event.sender.id"

    public val nodeId: String = "event.sender.node_id"

    public val avatarUrl: String = "event.sender.avatar_url"

    public val gravatarId: String = "event.sender.gravatar_id"

    public val url: String = "event.sender.url"

    public val htmlUrl: String = "event.sender.html_url"

    public val followersUrl: String = "event.sender.followers_url"

    public val followingUrl: String = "event.sender.following_url"

    public val gistsUrl: String = "event.sender.gists_url"

    public val starredUrl: String = "event.sender.starred_url"

    public val subscriptionsUrl: String = "event.sender.subscriptions_url"

    public val organizationsUrl: String = "event.sender.organizations_url"

    public val reposUrl: String = "event.sender.repos_url"

    public val eventsUrl: String = "event.sender.events_url"

    public val receivedEventsUrl: String = "event.sender.received_events_url"

    public val type: String = "event.sender.type"

    public val siteAdmin: String = "event.sender.site_admin"
}

public object Event {
    public val action: String = "event.action"

    public val number: String = "event.number"

    public val pullRequest: EventPullRequest = EventPullRequest

    public val repository: EventRepository = EventRepository

    public val sender: EventSender = EventSender
}
