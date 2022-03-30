package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#registry_package
 */
@Serializable
data class RegistryPackage(
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()
