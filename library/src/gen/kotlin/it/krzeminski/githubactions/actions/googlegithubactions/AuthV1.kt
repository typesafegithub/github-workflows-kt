// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.googlegithubactions

import it.krzeminski.githubactions.domain.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Authenticate to Google Cloud
 *
 * Authenticate to Google Cloud from GitHub Actions via Workload Identity
 * Federation or service account keys.
 *
 * [Action on GitHub](https://github.com/google-github-actions/auth)
 */
public data class AuthV1(
    /**
     * ID of the default project to use for future API calls and invocations. If
     * unspecified, this action will attempt to extract the value from other
     * inputs such as "service_account" or "credentials_json".
     */
    public val projectId: String? = null,
    /**
     * The full identifier of the Workload Identity Provider, including the
     * project number, pool name, and provider name. If provided, this must be
     * the full identifier which includes all parts, for example:
     * "projects/123456789/locations/global/workloadIdentityPools/my-pool/providers/my-provider".
     * This is mutually exclusive with "credentials_json".
     */
    public val workloadIdentityProvider: String? = null,
    /**
     * Email address or unique identifier of the Google Cloud service account for
     * which to generate credentials. This is required if
     * "workload_identity_provider" is specified.
     */
    public val serviceAccount: String? = null,
    /**
     * The value for the audience (aud) parameter in GitHub's generated OIDC
     * token. This value defaults to the value of "workload_identity_provider",
     * which is also the default value Google Cloud expects for the audience
     * parameter on the token.
     */
    public val audience: String? = null,
    /**
     * The Google Cloud JSON service account key to use for authentication. This
     * is mutually exclusive with "workload_identity_provider".
     */
    public val credentialsJson: String? = null,
    /**
     * If true, the action will securely generate a credentials file which can be
     * used for authentication via gcloud and Google Cloud SDKs.
     */
    public val createCredentialsFile: Boolean? = null,
    /**
     * If true, the action will export common environment variables which are
     * known to be consumed by popular downstream libraries and tools, including:
     *
     * - CLOUDSDK_PROJECT
     * - CLOUDSDK_CORE_PROJECT
     * - GCP_PROJECT
     * - GCLOUD_PROJECT
     * - GOOGLE_CLOUD_PROJECT
     *
     * If "create_credentials_file" is true, additional environment variables are
     * exported:
     *
     * - CLOUDSDK_AUTH_CREDENTIAL_FILE_OVERRIDE
     * - GOOGLE_APPLICATION_CREDENTIALS
     * - GOOGLE_GHA_CREDS_PATH
     *
     * If false, the action will not export any environment variables, meaning
     * future steps are unlikely to be automatically authenticated to Google
     * Cloud.
     */
    public val exportEnvironmentVariables: Boolean? = null,
    /**
     * Output format for the generated authentication token. For OAuth 2.0 access
     * tokens, specify "access_token". For OIDC tokens, specify "id_token". To
     * skip token generation, leave this value empty.
     */
    public val tokenFormat: AuthV1.TokenFormat? = null,
    /**
     * List of additional service account emails or unique identities to use for
     * impersonation in the chain.
     */
    public val delegates: List<String>? = null,
    /**
     * If true, the action will remove any created credentials from the
     * filesystem upon completion. This only applies if "create_credentials_file"
     * is true.
     */
    public val cleanupCredentials: Boolean? = null,
    /**
     * Desired lifetime duration of the access token, in seconds. This must be
     * specified as the number of seconds with a trailing "s" (e.g. 30s). This is
     * only valid when "token_format" is "access_token".
     */
    public val accessTokenLifetime: String? = null,
    /**
     * List of OAuth 2.0 access scopes to be included in the generated token.
     * This is only valid when "token_format" is "access_token".
     */
    public val accessTokenScopes: List<String>? = null,
    /**
     * Email address of a user to impersonate for Domain-Wide Delegation Access
     * tokens created for Domain-Wide Delegation cannot have a lifetime beyond 1
     * hour. This is only valid when "token_format" is "access_token".
     */
    public val accessTokenSubject: String? = null,
    /**
     * Number of times to retry a failed authentication attempt. This is useful
     * for automated pipelines that may execute before IAM permissions are fully propogated.
     */
    public val retries: Int? = null,
    /**
     * Delay time before trying another authentication attempt. This
     * is implemented using a fibonacci backoff method (e.g. 1-1-2-3-5).
     * This value defaults to 100 milliseconds when retries are greater than 0.
     */
    public val backoff: Int? = null,
    /**
     * Limits the retry backoff to the specified value.
     */
    public val backoffLimit: Int? = null,
    /**
     * The audience (aud) for the generated Google Cloud ID Token. This is only
     * valid when "token_format" is "id_token".
     */
    public val idTokenAudience: String? = null,
    /**
     * Optional parameter of whether to include the service account email in the
     * generated token. If true, the token will contain "email" and
     * "email_verified" claims. This is only valid when "token_format" is
     * "id_token".
     */
    public val idTokenIncludeEmail: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<AuthV1.Outputs>("google-github-actions", "auth", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            projectId?.let { "project_id" to it },
            workloadIdentityProvider?.let { "workload_identity_provider" to it },
            serviceAccount?.let { "service_account" to it },
            audience?.let { "audience" to it },
            credentialsJson?.let { "credentials_json" to it },
            createCredentialsFile?.let { "create_credentials_file" to it.toString() },
            exportEnvironmentVariables?.let { "export_environment_variables" to it.toString() },
            tokenFormat?.let { "token_format" to it.stringValue },
            delegates?.let { "delegates" to it.joinToString(",") },
            cleanupCredentials?.let { "cleanup_credentials" to it.toString() },
            accessTokenLifetime?.let { "access_token_lifetime" to it },
            accessTokenScopes?.let { "access_token_scopes" to it.joinToString(",") },
            accessTokenSubject?.let { "access_token_subject" to it },
            retries?.let { "retries" to it.toString() },
            backoff?.let { "backoff" to it.toString() },
            backoffLimit?.let { "backoff_limit" to it.toString() },
            idTokenAudience?.let { "id_token_audience" to it },
            idTokenIncludeEmail?.let { "id_token_include_email" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class TokenFormat(
        public val stringValue: String,
    ) {
        public object AccessToken : AuthV1.TokenFormat("access_token")

        public object IdToken : AuthV1.TokenFormat("id_token")

        public class Custom(
            customStringValue: String,
        ) : AuthV1.TokenFormat(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Provided or extracted value for the Google Cloud project ID.
         */
        public val projectId: String = "steps.$stepId.outputs.project_id"

        /**
         * Path on the local filesystem where the generated credentials file resides.
         * This is only available if "create_credentials_file" was set to true.
         */
        public val credentialsFilePath: String = "steps.$stepId.outputs.credentials_file_path"

        /**
         * The Google Cloud access token for calling other Google Cloud APIs. This is
         * only available when "token_format" is "access_token".
         */
        public val accessToken: String = "steps.$stepId.outputs.access_token"

        /**
         * The RFC3339 UTC "Zulu" format timestamp for the access token. This is only
         * available when "token_format" is "access_token".
         */
        public val accessTokenExpiration: String = "steps.$stepId.outputs.access_token_expiration"

        /**
         * The Google Cloud ID token. This is only available when "token_format" is
         * "id_token".
         */
        public val idToken: String = "steps.$stepId.outputs.id_token"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
