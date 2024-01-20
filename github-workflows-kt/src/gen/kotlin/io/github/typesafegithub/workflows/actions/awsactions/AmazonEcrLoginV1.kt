// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.awsactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
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
 *
 * @param httpProxy Proxy to use for the AWS SDK agent.
 * @param maskPassword Mask the docker password to prevent it being printed to action logs if debug
 * logging is enabled. NOTE: This will prevent the Docker password output from being shared between
 * separate jobs. Options: ['true', 'false']
 * @param registries A comma-delimited list of AWS account IDs that are associated with the ECR
 * Private registries. If you do not specify a registry, the default ECR Private registry is assumed.
 * If 'public' is given as input to 'registry-type', this input is ignored.
 * @param registryType Which ECR registry type to log into. Options: [private, public]
 * @param skipLogout Whether to skip explicit logout of the registries during post-job cleanup.
 * Exists for backward compatibility on self-hosted runners. Not recommended. Options: ['true',
 * 'false']
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: AmazonEcrLoginV2",
    replaceWith = ReplaceWith("AmazonEcrLoginV2"),
)
public data class AmazonEcrLoginV1 private constructor(
    /**
     * Proxy to use for the AWS SDK agent.
     */
    public val httpProxy: String? = null,
    /**
     * Mask the docker password to prevent it being printed to action logs if debug logging is
     * enabled. NOTE: This will prevent the Docker password output from being shared between separate
     * jobs. Options: ['true', 'false']
     */
    public val maskPassword: Boolean? = null,
    /**
     * A comma-delimited list of AWS account IDs that are associated with the ECR Private
     * registries. If you do not specify a registry, the default ECR Private registry is assumed. If
     * 'public' is given as input to 'registry-type', this input is ignored.
     */
    public val registries: List<String>? = null,
    /**
     * Which ECR registry type to log into. Options: [private, public]
     */
    public val registryType: AmazonEcrLoginV1.RegistryType? = null,
    /**
     * Whether to skip explicit logout of the registries during post-job cleanup. Exists for
     * backward compatibility on self-hosted runners. Not recommended. Options: ['true', 'false']
     */
    public val skipLogout: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<AmazonEcrLoginV1.Outputs>("aws-actions", "amazon-ecr-login", _customVersion ?:
        "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        httpProxy: String? = null,
        maskPassword: Boolean? = null,
        registries: List<String>? = null,
        registryType: AmazonEcrLoginV1.RegistryType? = null,
        skipLogout: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(httpProxy=httpProxy, maskPassword=maskPassword, registries=registries,
            registryType=registryType, skipLogout=skipLogout, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            httpProxy?.let { "http-proxy" to it },
            maskPassword?.let { "mask-password" to it.toString() },
            registries?.let { "registries" to it.joinToString(",") },
            registryType?.let { "registry-type" to it.stringValue },
            skipLogout?.let { "skip-logout" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

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
