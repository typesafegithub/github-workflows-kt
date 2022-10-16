package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#issues
 */
@Serializable
public data class Issues(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
