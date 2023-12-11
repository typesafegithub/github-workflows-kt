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
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: "Configure AWS Credentials" Action for GitHub Actions
 *
 * Configures AWS credentials for use in subsequent steps in a GitHub Action workflow
 *
 * [Action on GitHub](https://github.com/aws-actions/configure-aws-credentials)
 */
public data class ConfigureAwsCredentialsV4 private constructor(
    /**
     * AWS Region, e.g. us-east-2
     */
    public val awsRegion: String,
    /**
     * The Amazon Resource Name (ARN) of the role to assume. Use the provided credentials to assume
     * an IAM role and configure the Actions environment with the assumed role credentials rather than
     * with the provided credentials.
     */
    public val roleToAssume: String? = null,
    /**
     * AWS Access Key ID. Provide this key if you want to assume a role using access keys rather
     * than a web identity token.
     */
    public val awsAccessKeyId: String? = null,
    /**
     * AWS Secret Access Key. Required if aws-access-key-id is provided.
     */
    public val awsSecretAccessKey: String? = null,
    /**
     * AWS Session Token.
     */
    public val awsSessionToken: String? = null,
    /**
     * Use the web identity token file from the provided file system path in order to assume an IAM
     * role using a web identity, e.g. from within an Amazon EKS worker node.
     */
    public val webIdentityTokenFile: String? = null,
    /**
     * Use existing credentials from the environment to assume a new role, rather than providing
     * credentials as input.
     */
    public val roleChaining: Boolean? = null,
    /**
     * The audience to use for the OIDC provider
     */
    public val audience: String? = null,
    /**
     * Proxy to use for the AWS SDK agent
     */
    public val httpProxy: String? = null,
    /**
     * Whether to mask the AWS account ID for these credentials as a secret value. By default the
     * account ID will not be masked
     */
    public val maskAwsAccountId: Boolean? = null,
    /**
     * Role duration in seconds. Default is one hour.
     */
    public val roleDurationSeconds: Int? = null,
    /**
     * The external ID of the role to assume.
     */
    public val roleExternalId: String? = null,
    /**
     * Role session name (default: GitHubActions)
     */
    public val roleSessionName: String? = null,
    /**
     * Skip session tagging during role assumption
     */
    public val roleSkipSessionTagging: Boolean? = null,
    /**
     * Define an inline session policy to use when assuming a role
     */
    public val inlineSessionPolicy: String? = null,
    /**
     * Define a list of managed session policies to use when assuming a role
     */
    public val managedSessionPolicies: List<String>? = null,
    /**
     * Whether to set credentials as step output
     */
    public val outputCredentials: Boolean? = null,
    /**
     * Whether to unset the existing credentials in your runner. May be useful if you run this
     * action multiple times in the same job
     */
    public val unsetCurrentCredentials: String? = null,
    /**
     * Whether to disable the retry and backoff mechanism when the assume role call fails. By
     * default the retry mechanism is enabled
     */
    public val disableRetry: String? = null,
    /**
     * The maximum number of attempts it will attempt to retry the assume role call. By default it
     * will retry 12 times
     */
    public val retryMaxAttempts: Int? = null,
    /**
     * Some environments do not support special characters in AWS_SECRET_ACCESS_KEY. This option
     * will retry fetching credentials until the secret access key does not contain special characters.
     * This option overrides disable-retry and retry-max-attempts. This option is disabled by default
     */
    public val specialCharactersWorkaround: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<ConfigureAwsCredentialsV4.Outputs>("aws-actions", "configure-aws-credentials",
        _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        awsRegion: String,
        roleToAssume: String? = null,
        awsAccessKeyId: String? = null,
        awsSecretAccessKey: String? = null,
        awsSessionToken: String? = null,
        webIdentityTokenFile: String? = null,
        roleChaining: Boolean? = null,
        audience: String? = null,
        httpProxy: String? = null,
        maskAwsAccountId: Boolean? = null,
        roleDurationSeconds: Int? = null,
        roleExternalId: String? = null,
        roleSessionName: String? = null,
        roleSkipSessionTagging: Boolean? = null,
        inlineSessionPolicy: String? = null,
        managedSessionPolicies: List<String>? = null,
        outputCredentials: Boolean? = null,
        unsetCurrentCredentials: String? = null,
        disableRetry: String? = null,
        retryMaxAttempts: Int? = null,
        specialCharactersWorkaround: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(awsRegion=awsRegion, roleToAssume=roleToAssume, awsAccessKeyId=awsAccessKeyId,
            awsSecretAccessKey=awsSecretAccessKey, awsSessionToken=awsSessionToken,
            webIdentityTokenFile=webIdentityTokenFile, roleChaining=roleChaining, audience=audience,
            httpProxy=httpProxy, maskAwsAccountId=maskAwsAccountId,
            roleDurationSeconds=roleDurationSeconds, roleExternalId=roleExternalId,
            roleSessionName=roleSessionName, roleSkipSessionTagging=roleSkipSessionTagging,
            inlineSessionPolicy=inlineSessionPolicy, managedSessionPolicies=managedSessionPolicies,
            outputCredentials=outputCredentials, unsetCurrentCredentials=unsetCurrentCredentials,
            disableRetry=disableRetry, retryMaxAttempts=retryMaxAttempts,
            specialCharactersWorkaround=specialCharactersWorkaround, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "aws-region" to awsRegion,
            roleToAssume?.let { "role-to-assume" to it },
            awsAccessKeyId?.let { "aws-access-key-id" to it },
            awsSecretAccessKey?.let { "aws-secret-access-key" to it },
            awsSessionToken?.let { "aws-session-token" to it },
            webIdentityTokenFile?.let { "web-identity-token-file" to it },
            roleChaining?.let { "role-chaining" to it.toString() },
            audience?.let { "audience" to it },
            httpProxy?.let { "http-proxy" to it },
            maskAwsAccountId?.let { "mask-aws-account-id" to it.toString() },
            roleDurationSeconds?.let { "role-duration-seconds" to it.toString() },
            roleExternalId?.let { "role-external-id" to it },
            roleSessionName?.let { "role-session-name" to it },
            roleSkipSessionTagging?.let { "role-skip-session-tagging" to it.toString() },
            inlineSessionPolicy?.let { "inline-session-policy" to it },
            managedSessionPolicies?.let { "managed-session-policies" to it.joinToString("\n") },
            outputCredentials?.let { "output-credentials" to it.toString() },
            unsetCurrentCredentials?.let { "unset-current-credentials" to it },
            disableRetry?.let { "disable-retry" to it },
            retryMaxAttempts?.let { "retry-max-attempts" to it.toString() },
            specialCharactersWorkaround?.let { "special-characters-workaround" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The AWS account ID for the provided credentials
         */
        public val awsAccountId: String = "steps.$stepId.outputs.aws-account-id"

        /**
         * The AWS access key ID for the provided credentials
         */
        public val awsAccessKeyId: String = "steps.$stepId.outputs.aws-access-key-id"

        /**
         * The AWS secret access key for the provided credentials
         */
        public val awsSecretAccessKey: String = "steps.$stepId.outputs.aws-secret-access-key"

        /**
         * The AWS session token for the provided credentials
         */
        public val awsSessionToken: String = "steps.$stepId.outputs.aws-session-token"
    }
}
