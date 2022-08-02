package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Create(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
