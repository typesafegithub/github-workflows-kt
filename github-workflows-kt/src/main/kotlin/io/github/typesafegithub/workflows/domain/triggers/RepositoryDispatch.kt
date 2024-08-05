package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#repository_dispatch
 */
@Serializable
public data class RepositoryDispatch(
    val types: List<String>? = null,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
