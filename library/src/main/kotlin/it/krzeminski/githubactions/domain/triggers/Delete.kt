package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Delete(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
