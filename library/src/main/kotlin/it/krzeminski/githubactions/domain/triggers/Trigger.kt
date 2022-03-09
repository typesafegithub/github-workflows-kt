package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.FreeYamlArgs
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs
import it.krzeminski.githubactions.dsl.withFreeArgs

sealed class Trigger : HasFreeYamlArgs {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()

    fun types(types: List<String>): Trigger = withFreeArgs(
        "types" to types
    )
}
