package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.FakeList
import kotlin.String
import kotlin.collections.List

public object WorkflowDispatchEventContextInputs : ExpressionContext("github.event.inputs") {
    public val name: String = "github.event.inputs.name"
}

public object WorkflowDispatchEventContextOrganization :
    ExpressionContext("github.event.organization") {
    public val avatar_url: String = "github.event.organization.avatar_url"

    public val description: String = "github.event.organization.description"

    public val events_url: String = "github.event.organization.events_url"

    public val hooks_url: String = "github.event.organization.hooks_url"

    public val id: String = "github.event.organization.id"

    public val issues_url: String = "github.event.organization.issues_url"

    public val login: String = "github.event.organization.login"

    public val members_url: String = "github.event.organization.members_url"

    public val node_id: String = "github.event.organization.node_id"

    public val public_members_url: String = "github.event.organization.public_members_url"

    public val repos_url: String = "github.event.organization.repos_url"

    public val url: String = "github.event.organization.url"
}

public object WorkflowDispatchEventContextRepositoryOwner :
    ExpressionContext("github.event.repository.owner") {
    public val avatar_url: String = "github.event.repository.owner.avatar_url"

    public val events_url: String = "github.event.repository.owner.events_url"

    public val followers_url: String = "github.event.repository.owner.followers_url"

    public val following_url: String = "github.event.repository.owner.following_url"

    public val gists_url: String = "github.event.repository.owner.gists_url"

    public val gravatar_id: String = "github.event.repository.owner.gravatar_id"

    public val html_url: String = "github.event.repository.owner.html_url"

    public val id: String = "github.event.repository.owner.id"

    public val login: String = "github.event.repository.owner.login"

    public val node_id: String = "github.event.repository.owner.node_id"

    public val organizations_url: String = "github.event.repository.owner.organizations_url"

    public val received_events_url: String = "github.event.repository.owner.received_events_url"

    public val repos_url: String = "github.event.repository.owner.repos_url"

    public val site_admin: String = "github.event.repository.owner.site_admin"

    public val starred_url: String = "github.event.repository.owner.starred_url"

    public val subscriptions_url: String = "github.event.repository.owner.subscriptions_url"

    public val type: String = "github.event.repository.owner.type"

    public val url: String = "github.event.repository.owner.url"
}

public object WorkflowDispatchEventContextRepository : ExpressionContext("github.event.repository") {
    public val allow_forking: String = "github.event.repository.allow_forking"

    public val archive_url: String = "github.event.repository.archive_url"

    public val archived: String = "github.event.repository.archived"

    public val assignees_url: String = "github.event.repository.assignees_url"

    public val blobs_url: String = "github.event.repository.blobs_url"

    public val branches_url: String = "github.event.repository.branches_url"

    public val clone_url: String = "github.event.repository.clone_url"

    public val collaborators_url: String = "github.event.repository.collaborators_url"

    public val comments_url: String = "github.event.repository.comments_url"

    public val commits_url: String = "github.event.repository.commits_url"

    public val compare_url: String = "github.event.repository.compare_url"

    public val contents_url: String = "github.event.repository.contents_url"

    public val contributors_url: String = "github.event.repository.contributors_url"

    public val created_at: String = "github.event.repository.created_at"

    public val default_branch: String = "github.event.repository.default_branch"

    public val deployments_url: String = "github.event.repository.deployments_url"

    public val description: String = "github.event.repository.description"

    public val disabled: String = "github.event.repository.disabled"

    public val downloads_url: String = "github.event.repository.downloads_url"

    public val events_url: String = "github.event.repository.events_url"

    public val fork: String = "github.event.repository.fork"

    public val forks: String = "github.event.repository.forks"

    public val forks_count: String = "github.event.repository.forks_count"

    public val forks_url: String = "github.event.repository.forks_url"

    public val full_name: String = "github.event.repository.full_name"

    public val git_commits_url: String = "github.event.repository.git_commits_url"

    public val git_refs_url: String = "github.event.repository.git_refs_url"

    public val git_tags_url: String = "github.event.repository.git_tags_url"

    public val git_url: String = "github.event.repository.git_url"

    public val has_downloads: String = "github.event.repository.has_downloads"

    public val has_issues: String = "github.event.repository.has_issues"

    public val has_pages: String = "github.event.repository.has_pages"

    public val has_projects: String = "github.event.repository.has_projects"

    public val has_wiki: String = "github.event.repository.has_wiki"

    public val homepage: String = "github.event.repository.homepage"

    public val hooks_url: String = "github.event.repository.hooks_url"

    public val html_url: String = "github.event.repository.html_url"

    public val id: String = "github.event.repository.id"

    public val is_template: String = "github.event.repository.is_template"

    public val issue_comment_url: String = "github.event.repository.issue_comment_url"

    public val issue_events_url: String = "github.event.repository.issue_events_url"

    public val issues_url: String = "github.event.repository.issues_url"

    public val keys_url: String = "github.event.repository.keys_url"

    public val labels_url: String = "github.event.repository.labels_url"

    public val language: String = "github.event.repository.language"

    public val languages_url: String = "github.event.repository.languages_url"

    public val license: String = "github.event.repository.license"

    public val merges_url: String = "github.event.repository.merges_url"

    public val milestones_url: String = "github.event.repository.milestones_url"

    public val mirror_url: String = "github.event.repository.mirror_url"

    public val name: String = "github.event.repository.name"

    public val node_id: String = "github.event.repository.node_id"

    public val notifications_url: String = "github.event.repository.notifications_url"

    public val open_issues: String = "github.event.repository.open_issues"

    public val open_issues_count: String = "github.event.repository.open_issues_count"

    public val owner: WorkflowDispatchEventContextRepositoryOwner =
        WorkflowDispatchEventContextRepositoryOwner

    public val `private`: String = "github.event.repository.private"

    public val pulls_url: String = "github.event.repository.pulls_url"

    public val pushed_at: String = "github.event.repository.pushed_at"

    public val releases_url: String = "github.event.repository.releases_url"

    public val length: String = "github.event.repository.size"

    public val ssh_url: String = "github.event.repository.ssh_url"

    public val stargazers_count: String = "github.event.repository.stargazers_count"

    public val stargazers_url: String = "github.event.repository.stargazers_url"

    public val statuses_url: String = "github.event.repository.statuses_url"

    public val subscribers_url: String = "github.event.repository.subscribers_url"

    public val subscription_url: String = "github.event.repository.subscription_url"

    public val svn_url: String = "github.event.repository.svn_url"

    public val tags_url: String = "github.event.repository.tags_url"

    public val teams_url: String = "github.event.repository.teams_url"

    public val topics: List<String> = FakeList("github.event.repository.topics")

    public val trees_url: String = "github.event.repository.trees_url"

    public val updated_at: String = "github.event.repository.updated_at"

    public val url: String = "github.event.repository.url"

    public val visibility: String = "github.event.repository.visibility"

    public val watchers: String = "github.event.repository.watchers"

    public val watchers_count: String = "github.event.repository.watchers_count"
}

public object WorkflowDispatchEventContextSender : ExpressionContext("github.event.sender") {
    public val avatar_url: String = "github.event.sender.avatar_url"

    public val events_url: String = "github.event.sender.events_url"

    public val followers_url: String = "github.event.sender.followers_url"

    public val following_url: String = "github.event.sender.following_url"

    public val gists_url: String = "github.event.sender.gists_url"

    public val gravatar_id: String = "github.event.sender.gravatar_id"

    public val html_url: String = "github.event.sender.html_url"

    public val id: String = "github.event.sender.id"

    public val login: String = "github.event.sender.login"

    public val node_id: String = "github.event.sender.node_id"

    public val organizations_url: String = "github.event.sender.organizations_url"

    public val received_events_url: String = "github.event.sender.received_events_url"

    public val repos_url: String = "github.event.sender.repos_url"

    public val site_admin: String = "github.event.sender.site_admin"

    public val starred_url: String = "github.event.sender.starred_url"

    public val subscriptions_url: String = "github.event.sender.subscriptions_url"

    public val type: String = "github.event.sender.type"

    public val url: String = "github.event.sender.url"
}

public object WorkflowDispatchEventContext : ExpressionContext("github.event") {
    public val inputs: WorkflowDispatchEventContextInputs = WorkflowDispatchEventContextInputs

    public val organization: WorkflowDispatchEventContextOrganization =
        WorkflowDispatchEventContextOrganization

    public val ref: String = "github.event.ref"

    public val repository: WorkflowDispatchEventContextRepository =
        WorkflowDispatchEventContextRepository

    public val sender: WorkflowDispatchEventContextSender = WorkflowDispatchEventContextSender

    public val workflow: String = "github.event.workflow"
}
