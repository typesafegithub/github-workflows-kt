package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
public data class Status(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
