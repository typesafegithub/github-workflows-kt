// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.awsactions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
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
public data class AmazonEcsRenderTaskDefinitionV1(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<AmazonEcsRenderTaskDefinitionV1.Outputs>("aws-actions",
        "amazon-ecs-render-task-definition", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "task-definition" to taskDefinition,
            "container-name" to containerName,
            "image" to image,
            environmentVariables?.let { "environment-variables" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The path to the rendered task definition file
         */
        public val taskDefinition: String = "steps.$stepId.outputs.task-definition"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
