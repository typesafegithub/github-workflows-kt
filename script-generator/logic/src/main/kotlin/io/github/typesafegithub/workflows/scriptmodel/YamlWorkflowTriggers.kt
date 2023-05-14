package io.github.typesafegithub.workflows.scriptmodel

import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import kotlinx.serialization.Serializable

@Serializable
data class YamlWorkflowTriggers(
    val push: Push? = null,
    val pull_request: PullRequest? = null,
    val pull_request_target: PullRequestTarget? = null,
    val schedule: List<ScheduleValue>? = null,
    val workflow_dispatch: WorkflowDispatch? = null,
    val branch_protection_rule: YamlTrigger? = null,
    val check_run: YamlTrigger? = null,
    val check_suite: YamlTrigger? = null,
    val create: YamlTrigger? = null,
    val delete: YamlTrigger? = null,
    val deployment: YamlTrigger? = null,
    val deployment_status: YamlTrigger? = null,
    val discussion: YamlTrigger? = null,
    val discussion_comment: YamlTrigger? = null,
    val fork: YamlTrigger? = null,
    val gollum: YamlTrigger? = null,
    val issue_comment: YamlTrigger? = null,
    val issues: YamlTrigger? = null,
    val label: YamlTrigger? = null,
    val merge_group: YamlTrigger? = null,
    val milestone: YamlTrigger? = null,
    val page_build: YamlTrigger? = null,
    val project: YamlTrigger? = null,
    val project_card: YamlTrigger? = null,
    val project_column: YamlTrigger? = null,
    val public: YamlTrigger? = null,
    val pull_request_review: YamlTrigger? = null,
    val pull_request_review_comment: YamlTrigger? = null,
    val registry_package: YamlTrigger? = null,
    val release: YamlTrigger? = null,
    val repository_dispatch: YamlTrigger? = null,
    val status: YamlTrigger? = null,
    val watch: YamlTrigger? = null,
    val workflow_call: YamlTrigger? = null,
    val workflow_run: YamlTrigger? = null,
)
