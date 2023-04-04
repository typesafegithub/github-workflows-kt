package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
public data class Create(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
