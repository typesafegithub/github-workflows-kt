package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows#image_version
 */
@Serializable
public data class ImageVersion(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
