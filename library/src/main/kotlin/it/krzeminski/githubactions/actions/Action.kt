package it.krzeminski.githubactions.actions

import it.krzeminski.githubactions.dsl.FreeYamlArgs
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs

abstract class Action(
    open val actionOwner: String,
    open val actionName: String,
    open val actionVersion: String,
) : HasFreeYamlArgs {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()
    abstract fun toYamlArguments(): LinkedHashMap<String, String>
}

val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
