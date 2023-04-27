package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#merge_group
 */
@Serializable
public data class MergeGroup(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
