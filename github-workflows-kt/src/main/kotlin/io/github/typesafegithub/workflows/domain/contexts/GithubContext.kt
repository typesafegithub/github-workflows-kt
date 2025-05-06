@file:Suppress("ConstructorParameterNaming")

package io.github.typesafegithub.workflows.domain.contexts

import kotlinx.serialization.Serializable

@Serializable
public data class GithubContext(
    val repository: String,
    val sha: String,
    val ref: String? = null,
    val base_ref: String? = null,
    val event: GithubContextEvent,
    val event_name: String,
)

@Serializable
public data class GithubContextEvent(
    val after: String? = null,
)
