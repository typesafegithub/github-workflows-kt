package it.krzeminski.githubactions.domain.actions

import it.krzeminski.githubactions.domain.actions.Action.Outputs

/**
 * CustomAction can be used when there is no type-safe wrapper action
 * and a quickly untyped wrapper is needed to fill the blank.
 *
 * Consider adding first-class support for your action! See CONTRIBUTING.md.
 */
public class CustomAction(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
    public val inputs: Map<String, String>,
) : Action<Outputs>(actionOwner, actionName, actionVersion) {
    override fun toYamlArguments(): LinkedHashMap<String, String> =
        LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Outputs =
        Outputs(stepId)
}
