package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_call
 */
@Serializable
data class WorkflowCall(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
