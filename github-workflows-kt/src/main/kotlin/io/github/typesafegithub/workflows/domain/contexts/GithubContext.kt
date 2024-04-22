package io.github.typesafegithub.workflows.domain.contexts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GithubContext(
    val repository: String,
    val sha: String,
    val event: Event,
    @SerialName("event_name")
    val eventName: String,
) {
    @Serializable
    public data class Event(
        val after: String?,
    )
}
