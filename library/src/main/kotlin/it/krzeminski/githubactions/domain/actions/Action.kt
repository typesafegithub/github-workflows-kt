package it.krzeminski.githubactions.domain.actions

import it.krzeminski.githubactions.domain.actions.Action.Outputs

public abstract class Action<out OUTPUTS : Outputs>(
    public open val actionOwner: String,
    public open val actionName: String,
    public open val actionVersion: String,
) {
    public abstract fun toYamlArguments(): LinkedHashMap<String, String>
    public abstract fun buildOutputObject(stepId: String): OUTPUTS

    public open class Outputs(private val stepId: String) {
        public operator fun get(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}

internal val Action<*>.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"
