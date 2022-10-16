package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#gollum
 */
@Serializable
public data class Gollum(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
