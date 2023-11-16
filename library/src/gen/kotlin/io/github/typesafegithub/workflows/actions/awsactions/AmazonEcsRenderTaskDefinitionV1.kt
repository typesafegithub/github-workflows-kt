// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.awsactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Amazon ECS "Render Task Definition" Action for GitHub Actions
 *
 * Inserts a container image URI into a container definition in an Amazon ECS task definition JSON
 * file, creating a new file.
 *
 * [Action on GitHub](https://github.com/aws-actions/amazon-ecs-render-task-definition)
 */
public data class AmazonEcsRenderTaskDefinitionV1 private constructor(
    /**
     * The path to the ECS task definition JSON file
     */
    public val taskDefinition: String,
    /**
     * The name of the container defined in the containerDefinitions section of the ECS task
     * definition
     */
    public val containerName: String,
    /**
     * The URI of the container image to insert into the ECS task definition
     */
    public val image: String,
    /**
     * Variables to add to the container. Each variable is of the form KEY=value, you can specify
     * multiple variables with multi-line YAML strings.
     */
    public val environmentVariables: List<String>? = null,
    /**
     * Create/Override logDriver inside logConfiguration
     */
    public val logConfigurationLogDriver: String? = null,
    /**
     * Create/Override options inside logConfiguration. Each variable is of the form key=value, you
     * can specify multiple variables with multi-line YAML strings.
     */
    public val logConfigurationOptions: String? = null,
    /**
     * Create/Override options inside dockerLabels. Each variable is key=value, you can specify
     * multiple variables with multi-line YAML.
     */
    public val dockerLabels: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<AmazonEcsRenderTaskDefinitionV1.Outputs>("aws-actions",
        "amazon-ecs-render-task-definition", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        taskDefinition: String,
        containerName: String,
        image: String,
        environmentVariables: List<String>? = null,
        logConfigurationLogDriver: String? = null,
        logConfigurationOptions: String? = null,
        dockerLabels: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(taskDefinition=taskDefinition, containerName=containerName, image=image,
            environmentVariables=environmentVariables,
            logConfigurationLogDriver=logConfigurationLogDriver,
            logConfigurationOptions=logConfigurationOptions, dockerLabels=dockerLabels,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "task-definition" to taskDefinition,
            "container-name" to containerName,
            "image" to image,
            environmentVariables?.let { "environment-variables" to it.joinToString("\n") },
            logConfigurationLogDriver?.let { "log-configuration-log-driver" to it },
            logConfigurationOptions?.let { "log-configuration-options" to it },
            dockerLabels?.let { "docker-labels" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The path to the rendered task definition file
         */
        public val taskDefinition: String = "steps.$stepId.outputs.task-definition"
    }
}
