package it.krzeminski.githubactions.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Using concurrency allows running single workflow/job at a time
 * See https://docs.github.com/en/actions/using-jobs/using-concurrency
 * See https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#jobsjob_idconcurrency
 */
@Serializable
public data class Concurrency(
    val group: String,
    @SerialName("cancel-in-progress")
    val cancelInProgress: Boolean = false,
)
