package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#release
 */
@Serializable
data class Release(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
