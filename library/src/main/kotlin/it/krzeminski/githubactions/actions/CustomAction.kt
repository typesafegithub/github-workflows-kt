package it.krzeminski.githubactions.actions

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
) : ActionWithOutputs<CustomAction.Output>(actionOwner, actionName, actionVersion) {
    override fun toYamlArguments(): LinkedHashMap<String, String> =
        LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Output =
        Output(stepId)

    public class Output(private val stepId: String) {
        public operator fun get(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
