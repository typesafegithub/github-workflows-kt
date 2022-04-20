package it.krzeminski.githubactions.wrappergenerator.payload

import kotlin.String

public val github: Github = Github

public object Github {
    public val token: String = "github.token"

    public val job: String = "github.job"

    public val ref: String = "github.ref"

    public val sha: String = "github.sha"

    public val repository: String = "github.repository"

    public val repositoryOwner: String = "github.repository_owner"

    public val repositoryUrl: String = "github.repositoryUrl"

    public val runId: String = "github.run_id"

    public val runNumber: String = "github.run_number"

    public val retentionDays: String = "github.retention_days"

    public val runAttempt: String = "github.run_attempt"

    public val actor: String = "github.actor"

    public val workflow: String = "github.workflow"

    public val headRef: String = "github.head_ref"

    public val baseRef: String = "github.base_ref"

    public val eventName: String = "github.event_name"

    public val serverUrl: String = "github.server_url"

    public val apiUrl: String = "github.api_url"

    public val graphqlUrl: String = "github.graphql_url"

    public val refName: String = "github.ref_name"

    public val refProtected: String = "github.ref_protected"

    public val refType: String = "github.ref_type"

    public val secretSource: String = "github.secret_source"

    public val workspace: String = "github.workspace"

    public val action: String = "github.action"

    public val eventPath: String = "github.event_path"

    public val actionRepository: String = "github.action_repository"

    public val actionRef: String = "github.action_ref"

    public val path: String = "github.path"

    public val env: String = "github.env"
}
