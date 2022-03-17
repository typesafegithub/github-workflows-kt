package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.dsl.withFreeArgs

sealed class Trigger : HasCustomArguments {
    override val _customArguments: Map<String, CustomValue> =
        mutableListOf()

    fun types(vararg types: String): Trigger = withFreeArgs(
        "types" to types.toList()
    )
}
