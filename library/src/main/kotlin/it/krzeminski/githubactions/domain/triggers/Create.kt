package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue

data class Create(
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()
