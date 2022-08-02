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
 * Logs in the local Docker client to one or more Amazon ECR Private registries or an Amazon ECR
 * Public registry
 *
 * [Action on GitHub](https://github.com/aws-actions/amazon-ecr-login)
 */
public class AmazonEcrLoginV1(
    /**
     * A comma-delimited list of AWS account IDs that are associated with the ECR Private
     * registries. If you do not specify a registry, the default ECR Private registry is assumed. If
     * 'public' is given as input to 'registry-type', this input is ignored.
     */
    public val registries: List<String>? = null,
    /**
     * Whether to skip explicit logout of the registries during post-job cleanup. Exists for
     * backward compatibility on self-hosted runners. Not recommended.
     */
    public val skipLogout: Boolean? = null,
    /**
     * Which ECR registry type to log into. Options: 'private', 'public'
     */
    public val registryType: AmazonEcrLoginV1.RegistryType? = null,
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
            registryType?.let { "registry-type" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public sealed class RegistryType(
        public val stringValue: String,
    ) {
        public object Private : AmazonEcrLoginV1.RegistryType("private")

        public object Public : AmazonEcrLoginV1.RegistryType("public")

        public class Custom(
            customStringValue: String,
        ) : AmazonEcrLoginV1.RegistryType(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The URI of the ECR Private or ECR Public registry. If logging into multiple registries on
         * ECR Private, this output will not be set.
         */
        public val registry: String = "steps.$stepId.outputs.registry"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
