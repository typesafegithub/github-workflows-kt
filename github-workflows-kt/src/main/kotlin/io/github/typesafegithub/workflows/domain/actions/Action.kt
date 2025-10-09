package io.github.typesafegithub.workflows.domain.actions

import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import io.github.typesafegithub.workflows.yaml.toYaml

public abstract class Action<out OUTPUTS : Outputs> {
    public abstract fun toYamlArguments(): LinkedHashMap<String, String>

    public val yamlArgumentsString: String get() = toYamlArguments().toYaml()

    public abstract fun buildOutputObject(stepId: String): OUTPUTS

    public abstract val usesString: String

    public open fun isCompatibleWithLibraryVersion(libraryVersion: String): Boolean = true

    public val comment: String? = null

    public open class Outputs(
        private val stepId: String,
    ) {
        public operator fun get(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}

public abstract class RegularAction<out OUTPUTS : Outputs>(
    public open val actionOwner: String,
    public open val actionName: String,
    public open val actionVersion: String,
) : Action<OUTPUTS>() {
    override val usesString: String
        get() = "$actionOwner/$actionName@$actionVersion"
}

public abstract class LocalAction<out OUTPUTS : Outputs>(
    public open val actionPath: String,
) : Action<OUTPUTS>() {
    override val usesString: String
        get() = actionPath
}

public abstract class DockerAction<out OUTPUTS : Outputs>(
    public open val actionImage: String,
    public open val actionTag: String,
    public open val actionHost: String? = null,
) : Action<OUTPUTS>() {
    override val usesString: String
        get() = "docker://${if (actionHost == null) "" else "$actionHost/"}$actionImage:$actionTag"
}
