package it.krzeminski.githubactions.actions

/**
 * CustomAction can be used when there is no type-safe wrapper action
 * and a quickly untyped wrapper is needed to fill the blank.
 *
 * Consider contributing a proper wrapper to the library, see [WrappersToGenerate.kt]
 */
class CustomAction(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
    val inputs: Map<String, String>,
) : ActionWithOutputs<CustomAction.Output>(actionOwner, actionName, actionVersion) {
    override fun toYamlArguments(): LinkedHashMap<String, String> =
        LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Output =
        Output(stepId)

    class Output(private val stepId: String) {
        operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
