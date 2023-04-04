package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#deployment_status
 */
@Serializable
public data class DeploymentStatus(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
