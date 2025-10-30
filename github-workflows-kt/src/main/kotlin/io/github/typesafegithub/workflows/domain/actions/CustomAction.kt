package io.github.typesafegithub.workflows.domain.actions

import io.github.typesafegithub.workflows.domain.actions.Action.Outputs

/**
 * CustomAction can be used when there is no type-safe binding action
 * and a quickly untyped binding is needed to fill the blank.
 *
 * Consider adding first-class support for your action! See CONTRIBUTING.md.
 */
public data class CustomAction(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
    override val intendedVersion: String? = null,
    public val inputs: Map<String, String> = emptyMap(),
) : RegularAction<Outputs>(actionOwner, actionName, actionVersion) {
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)
}

public data class CustomLocalAction(
    override val actionPath: String,
    public val inputs: Map<String, String> = emptyMap(),
) : LocalAction<Outputs>(actionPath) {
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)
}

public data class CustomDockerAction(
    override val actionImage: String,
    override val actionTag: String,
    public val inputs: Map<String, String> = emptyMap(),
    override val actionHost: String? = null,
) : DockerAction<Outputs>(actionImage, actionTag, actionHost) {
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(inputs)

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)
}
