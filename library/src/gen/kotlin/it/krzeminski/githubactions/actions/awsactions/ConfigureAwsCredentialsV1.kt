// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.awsactions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: "Configure AWS Credentials" Action For GitHub Actions
 *
 * Configure AWS credential and region environment variables for use with the AWS CLI and AWS SDKs
 *
 * [Action on GitHub](https://github.com/aws-actions/configure-aws-credentials)
 */
public class ConfigureAwsCredentialsV1(
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
     * Role duration in seconds (default: 6 hours)
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<ConfigureAwsCredentialsV1.Outputs>(
    "aws-actions", "configure-aws-credentials",
    _customVersion ?: "v1"
) {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
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
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The AWS account ID for the provided credentials
         */
        public val awsAccountId: String = "steps.$stepId.outputs.aws-account-id"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
