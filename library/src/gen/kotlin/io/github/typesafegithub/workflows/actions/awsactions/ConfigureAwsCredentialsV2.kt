// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.awsactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Configure AWS Credentials For GitHub Actions
 *
 * Configure AWS credential and region environment variables for use with the AWS CLI and AWS SDKs
 *
 * [Action on GitHub](https://github.com/aws-actions/configure-aws-credentials)
 */
public data class ConfigureAwsCredentialsV2 private constructor(
    /**
     * The audience to use for the OIDC provider
     */
    public val audience: String? = null,
    /**
     * AWS Access Key ID. This input is required if running in the GitHub hosted environment. It is
     * optional if running in a self-hosted environment that already has AWS credentials, for example
     * on an EC2 instance.
     */
    public val awsAccessKeyId: String? = null,
    /**
     * AWS Secret Access Key. This input is required if running in the GitHub hosted environment. It
     * is optional if running in a self-hosted environment that already has AWS credentials, for
     * example on an EC2 instance.
     */
    public val awsSecretAccessKey: String? = null,
    /**
     * AWS Session Token
     */
    public val awsSessionToken: String? = null,
    /**
     * AWS Region, e.g. us-east-2
     */
    public val awsRegion: String,
    /**
     * Whether to set the AWS account ID for these credentials as a secret value, so that it is
     * masked in logs. Valid values are 'true' and 'false'. Defaults to true
     */
    public val maskAwsAccountId: String? = null,
    /**
     * Use the provided credentials to assume an IAM role and configure the Actions environment with
     * the assumed role credentials rather than with the provided credentials
     */
    public val roleToAssume: String? = null,
    /**
     * Use the web identity token file from the provided file system path in order to assume an IAM
     * role using a web identity. E.g., from within an Amazon EKS worker node
     */
    public val webIdentityTokenFile: String? = null,
    /**
     * Role duration in seconds (default: 6 hours, 1 hour for OIDC/specified aws-session-token)
     */
    public val roleDurationSeconds: Int? = null,
    /**
     * Role session name (default: GitHubActions)
     */
    public val roleSessionName: String? = null,
    /**
     * The external ID of the role to assume
     */
    public val roleExternalId: String? = null,
    /**
     * Skip session tagging during role assumption
     */
    public val roleSkipSessionTagging: Boolean? = null,
    /**
     * Proxy to use for the AWS SDK agent
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
) : Action<ConfigureAwsCredentialsV2.Outputs>("aws-actions", "configure-aws-credentials",
        _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        audience: String? = null,
        awsAccessKeyId: String? = null,
        awsSecretAccessKey: String? = null,
        awsSessionToken: String? = null,
        awsRegion: String,
        maskAwsAccountId: String? = null,
        roleToAssume: String? = null,
        webIdentityTokenFile: String? = null,
        roleDurationSeconds: Int? = null,
        roleSessionName: String? = null,
        roleExternalId: String? = null,
        roleSkipSessionTagging: Boolean? = null,
        httpProxy: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(audience=audience, awsAccessKeyId=awsAccessKeyId,
            awsSecretAccessKey=awsSecretAccessKey, awsSessionToken=awsSessionToken,
            awsRegion=awsRegion, maskAwsAccountId=maskAwsAccountId, roleToAssume=roleToAssume,
            webIdentityTokenFile=webIdentityTokenFile, roleDurationSeconds=roleDurationSeconds,
            roleSessionName=roleSessionName, roleExternalId=roleExternalId,
            roleSkipSessionTagging=roleSkipSessionTagging, httpProxy=httpProxy,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            audience?.let { "audience" to it },
            awsAccessKeyId?.let { "aws-access-key-id" to it },
            awsSecretAccessKey?.let { "aws-secret-access-key" to it },
            awsSessionToken?.let { "aws-session-token" to it },
            "aws-region" to awsRegion,
            maskAwsAccountId?.let { "mask-aws-account-id" to it },
            roleToAssume?.let { "role-to-assume" to it },
            webIdentityTokenFile?.let { "web-identity-token-file" to it },
            roleDurationSeconds?.let { "role-duration-seconds" to it.toString() },
            roleSessionName?.let { "role-session-name" to it },
            roleExternalId?.let { "role-external-id" to it },
            roleSkipSessionTagging?.let { "role-skip-session-tagging" to it.toString() },
            httpProxy?.let { "http-proxy" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The AWS account ID for the provided credentials
         */
        public val awsAccountId: String = "steps.$stepId.outputs.aws-account-id"
    }
}
