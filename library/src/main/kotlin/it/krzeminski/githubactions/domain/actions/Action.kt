package it.krzeminski.githubactions.domain.actions

public abstract class Action(
    public open val actionOwner: String,
    public open val actionName: String,
    public open val actionVersion: String,
) {
    public abstract fun toYamlArguments(): LinkedHashMap<String, String>
}

internal val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
