package it.krzeminski.githubactions.actions

abstract class Action(
    open val actionOwner: String,
    open val actionName: String,
    open val actionVersion: String,
) {
    abstract fun toYamlArguments(): LinkedHashMap<String, String>
}

val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
