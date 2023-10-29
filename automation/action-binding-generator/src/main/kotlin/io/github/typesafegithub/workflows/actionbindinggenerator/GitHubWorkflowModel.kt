package io.github.typesafegithub.workflows.actionbindinggenerator

import kotlinx.serialization.Serializable

@Serializable
internal data class Workflow(
    val jobs: Map<String, Job>,
)

@Serializable
internal data class Job(
    val steps: List<Step>,
)

@Serializable
internal data class Step(
    val uses: String? = null,
)
