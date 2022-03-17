package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#project
 */
data class Project(
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()
