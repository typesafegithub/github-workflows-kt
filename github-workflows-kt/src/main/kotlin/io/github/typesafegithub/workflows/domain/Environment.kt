package io.github.typesafegithub.workflows.domain

/**
 * https://docs.github.com/en/actions/using-jobs/using-environments-for-jobs
 */
public data class Environment(
    val name: String,
    val url: String? = null,
)
