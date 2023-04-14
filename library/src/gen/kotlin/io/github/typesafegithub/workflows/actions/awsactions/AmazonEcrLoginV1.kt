// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.awsactions

import io.github.typesafegithub.workflows.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class AmazonEcrLoginV1 private constructor(
    /**
     * A comma-delimited list of AWS account IDs that are associated with the ECR Private
     * registries. If you do not specify a registry, the default ECR Private registry is assumed. If
     * 'public' is given as input to 'registry-type', this input is ignored.
     */
    public val registries: List<String>? = null,
    /**
     * Whether to skip explicit logout of the registries during post-job cleanup. Exists for
     * backward compatibility on self-hosted runners. Not recommended. Options: ['true', 'false']
     */
    public val skipLogout: Boolean? = null,
    /**
     * Which ECR registry type to log into. Options: [private, public]
     */
    public val registryType: AmazonEcrLoginV1.RegistryType? = null,
    /**
     * Proxy to use for the AWS SDK agent.
     */
    public val httpProxy: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<AmazonEcrLoginV1.Outputs>("aws-actions", "amazon-ecr-login", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        registries: List<String>? = null,
        skipLogout: Boolean? = null,
        registryType: AmazonEcrLoginV1.RegistryType? = null,
        httpProxy: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(registries=registries, skipLogout=skipLogout, registryType=registryType,
            httpProxy=httpProxy, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            registries?.let { "registries" to it.joinToString(",") },
            skipLogout?.let { "skip-logout" to it.toString() },
            registryType?.let { "registry-type" to it.stringValue },
            httpProxy?.let { "http-proxy" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

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
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The URI of the ECR Private or ECR Public registry. If logging into multiple registries on
         * ECR Private, this output will not be set.
         */
        public val registry: String = "steps.$stepId.outputs.registry"
    }
}
