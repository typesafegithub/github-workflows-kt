package it.krzeminski.githubactions.actions

import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments

abstract class Action(
    open val actionOwner: String,
    open val actionName: String,
    open val actionVersion: String,
) : HasCustomArguments {
    override val _customArguments: Map<String, CustomValue> =
        mutableListOf()

    abstract fun toYamlArguments(): LinkedHashMap<String, String>
}

val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
