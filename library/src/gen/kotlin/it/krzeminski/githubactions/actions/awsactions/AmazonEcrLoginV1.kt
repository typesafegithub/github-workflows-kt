// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.awsactions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Amazon ECR "Login" Action for GitHub Actions
 *
 * Logs in the local Docker client to one or more ECR registries
 *
 * [Action on GitHub](https://github.com/aws-actions/amazon-ecr-login)
 */
public class AmazonEcrLoginV1(
    /**
     * A comma-delimited list of AWS account IDs that are associated with the ECR registries. If you
     * do not specify a registry, the default ECR registry is assumed.
     */
    public val registries: List<String>? = null,
    /**
     * Whether to skip explicit logout of the registries during post-job cleanup. Exists for
     * backward compatibility on self-hosted runners. Not recommended.
     */
    public val skipLogout: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<AmazonEcrLoginV1.Outputs>(
    "aws-actions", "amazon-ecr-login",
    _customVersion
        ?: "v1"
) {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            registries?.let { "registries" to it.joinToString(",") },
            skipLogout?.let { "skip-logout" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The URI of the ECR registry i.e. aws_account_id.dkr.ecr.region.amazonaws.com. If multiple
         * registries are provided as inputs, this output will not be set.
         */
        public val registry: String = "steps.$stepId.outputs.registry"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
