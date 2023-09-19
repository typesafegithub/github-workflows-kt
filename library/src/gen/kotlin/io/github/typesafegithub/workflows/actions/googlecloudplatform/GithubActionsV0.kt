// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.googlecloudplatform

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
 * Action: Set up gcloud Cloud SDK environment
 *
 * Downloads, installs, and configures a Google Cloud SDK environment.
 * Adds the `gcloud` CLI command to the $PATH.
 *
 * [Action on GitHub](https://github.com/GoogleCloudPlatform/github-actions)
 */
@Deprecated(
    message = "This action has a newer major version: GithubActionsV1",
    replaceWith = ReplaceWith("GithubActionsV1"),
)
public data class GithubActionsV0 private constructor(
    /**
     * Version of the gcloud SDK to install. If unspecified or set to "latest",
     * the latest available gcloud SDK version for the target platform will be
     * installed. Example: "290.0.1".
     */
    public val version: String? = null,
    /**
     * Service account email address to use for authentication. This is required
     * for legacy .p12 keys but can be omitted for .json keys. This is usually of
     * the format <name>@<project-id>.iam.gserviceaccount.com.
     */
    public val serviceAccountEmail: String? = null,
    /**
     * Service account key to use for authentication. This should be the JSON
     * formatted private key which can be exported from the Cloud Console. The
     * value can be raw or base64-encoded.
     */
    public val serviceAccountKey: String? = null,
    /**
     * ID of the Google Cloud project. If provided, this will configure gcloud to
     * use this project ID by default for commands. Individual commands can still
     * override the project using the --project flag which takes precedence.
     */
    public val projectId: String? = null,
    /**
     * List of Cloud SDK components to install
     */
    public val installComponents: List<String>? = null,
    /**
     * Export the provided credentials as Google Default Application Credentials.
     * This will make the credentials available to later steps via the
     * GOOGLE_APPLICATION_CREDENTIALS environment variable. Future steps that
     * consume Default Application Credentials will automatically detect and use
     * these credentials.
     */
    public val exportDefaultCredentials: Boolean? = null,
    /**
     * The path and name of the file to which to write the shared default
     * credentials. This option is only valid when
     * export_default_credentials=true. By default, the credentials will be
     * written to a new file in the root of GITHUB_WORKSPACE.
     */
    public val credentialsFilePath: String? = null,
    /**
     * If true, the action will remove any generated credentials from the
     * filesystem upon completion.
     */
    public val cleanupCredentials: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("GoogleCloudPlatform", "github-actions", _customVersion ?: "v0") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        version: String? = null,
        serviceAccountEmail: String? = null,
        serviceAccountKey: String? = null,
        projectId: String? = null,
        installComponents: List<String>? = null,
        exportDefaultCredentials: Boolean? = null,
        credentialsFilePath: String? = null,
        cleanupCredentials: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(version=version, serviceAccountEmail=serviceAccountEmail,
            serviceAccountKey=serviceAccountKey, projectId=projectId,
            installComponents=installComponents, exportDefaultCredentials=exportDefaultCredentials,
            credentialsFilePath=credentialsFilePath, cleanupCredentials=cleanupCredentials,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            version?.let { "version" to it },
            serviceAccountEmail?.let { "service_account_email" to it },
            serviceAccountKey?.let { "service_account_key" to it },
            projectId?.let { "project_id" to it },
            installComponents?.let { "install_components" to it.joinToString("\n") },
            exportDefaultCredentials?.let { "export_default_credentials" to it.toString() },
            credentialsFilePath?.let { "credentials_file_path" to it },
            cleanupCredentials?.let { "cleanup_credentials" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
