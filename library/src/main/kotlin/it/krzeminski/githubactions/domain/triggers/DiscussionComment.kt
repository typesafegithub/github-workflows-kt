package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionComment(
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()
