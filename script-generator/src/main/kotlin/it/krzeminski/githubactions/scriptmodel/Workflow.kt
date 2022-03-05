package it.krzeminski.githubactions.scriptmodel

import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Workflow(
    val name: String,
    val on: WorkflowOn,
    val jobs: Map<String, Job> = emptyMap(),
) {
    override fun toString() = """
       name: $name
       on:
         $on
       jobs:
         ${jobs.entries.joinToString(separator = "\n         ")}
        """.trimIndent()
}

@Serializable
data class WorkflowOn(
    val push: Push? = null,
    val pull_request: PullRequest? = null,
    val pull_request_target: PullRequestTarget? = null,
    val schedule: List<ScheduleValue>? = null,
    val workflow_dispatch: WorkflowDispatch? = null,
)

@Serializable
data class Job(
    val name: String? = null,
    @SerialName("runs-on")
    val runsOn: String,
    val steps: List<SerializedStep>,
    val needs: List<String> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
)

@Serializable
data class SerializedStep(
    val id: String? = null,
    val name: String? = null,
    val run: String? = null,
    val uses: String? = null,
    val with: LinkedHashMap<String, String?> = linkedMapOf(),
    val env: LinkedHashMap<String, String?> = linkedMapOf(),
    @SerialName("if")
    val condition: String? = null,
)

@Serializable
data class ScheduleValue(
    val cron: String,
)