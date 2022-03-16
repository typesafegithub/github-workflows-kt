package it.krzeminski.githubactions.actions

/**
 * Used by the script generator when no Action Wrapper can be found
 */
class MissingAction(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
    val with: Map<String, String>,
) : Action(actionOwner, actionName, actionVersion) {
    override fun toYamlArguments(): LinkedHashMap<String, String> =
        LinkedHashMap(with)
}
