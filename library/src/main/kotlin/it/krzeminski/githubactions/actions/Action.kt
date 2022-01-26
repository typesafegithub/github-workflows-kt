package it.krzeminski.githubactions.actions

abstract class Action(
    val actionOwner: String,
    val actionName: String,
    val actionVersion: String,
) {
    abstract fun toYamlArguments(): LinkedHashMap<String, String>
}

val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
