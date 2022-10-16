package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_run
 */
@Serializable
public data class WorkflowRun(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
