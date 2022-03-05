package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.FreeYamlArgs
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs

sealed class Trigger : HasFreeYamlArgs {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()
}
