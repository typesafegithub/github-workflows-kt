package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#watch
 */
@Serializable
data class Watch(
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()
