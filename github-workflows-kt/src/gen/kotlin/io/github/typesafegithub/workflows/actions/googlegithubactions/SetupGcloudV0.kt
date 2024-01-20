// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.googlegithubactions

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
 * [Action on GitHub](https://github.com/google-github-actions/setup-gcloud)
 *
 * @param version Version of the gcloud SDK to install. If unspecified or set to "latest",
 * the latest available gcloud SDK version for the target platform will be
 * installed. Example: "290.0.1".
 * @param serviceAccountEmail Service account email address to use for authentication. This is
 * required
 * for legacy .p12 keys but can be omitted for .json keys. This is usually of
 * the format <name>@<project-id>.iam.gserviceaccount.com.
 * @param serviceAccountKey Service account key to use for authentication. This should be the JSON
 * formatted private key which can be exported from the Cloud Console. The
 * value can be raw or base64-encoded.
 * @param projectId ID of the Google Cloud project. If provided, this will configure gcloud to
 * use this project ID by default for commands. Individual commands can still
 * override the project using the --project flag which takes precedence.
 * @param installComponents List of Cloud SDK components to install
 * @param exportDefaultCredentials Export the provided credentials as Google Default Application
 * Credentials.
 * This will make the credentials available to later steps via the
 * GOOGLE_APPLICATION_CREDENTIALS environment variable. Future steps that
 * consume Default Application Credentials will automatically detect and use
 * these credentials.
 * @param credentialsFilePath The path and name of the file to which to write the shared default
 * credentials. This option is only valid when
 * export_default_credentials=true. By default, the credentials will be
 * written to a new file in the root of GITHUB_WORKSPACE.
 * @param cleanupCredentials If true, the action will remove any generated credentials from the
 * filesystem upon completion.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: SetupGcloudV2",
    replaceWith = ReplaceWith("SetupGcloudV2"),
)
public data class SetupGcloudV0 private constructor(
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
    public val installComponents: List<SetupGcloudV0.Component>? = null,
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("google-github-actions", "setup-gcloud", _customVersion ?: "v0") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        version: String? = null,
        serviceAccountEmail: String? = null,
        serviceAccountKey: String? = null,
        projectId: String? = null,
        installComponents: List<SetupGcloudV0.Component>? = null,
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
            installComponents?.let { "install_components" to it.joinToString(",") { it.stringValue }
                    },
            exportDefaultCredentials?.let { "export_default_credentials" to it.toString() },
            credentialsFilePath?.let { "credentials_file_path" to it },
            cleanupCredentials?.let { "cleanup_credentials" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Component(
        public val stringValue: String,
    ) {
        public object Alpha : SetupGcloudV0.Component("alpha")

        public object AnthosAuth : SetupGcloudV0.Component("anthos-auth")

        public object Appctl : SetupGcloudV0.Component("appctl")

        public object AppEngineGo : SetupGcloudV0.Component("app-engine-go")

        public object AppEngineJava : SetupGcloudV0.Component("app-engine-java")

        public object AppEnginePython : SetupGcloudV0.Component("app-engine-python")

        public object AppEnginePythonExtras : SetupGcloudV0.Component("app-engine-python-extras")

        public object Beta : SetupGcloudV0.Component("beta")

        public object Bigtable : SetupGcloudV0.Component("bigtable")

        public object Bq : SetupGcloudV0.Component("bq")

        public object BundledPython3Unix : SetupGcloudV0.Component("bundled-python3-unix")

        public object Cbt : SetupGcloudV0.Component("cbt")

        public object CloudBuildLocal : SetupGcloudV0.Component("cloud-build-local")

        public object CloudDatastoreEmulator : SetupGcloudV0.Component("cloud-datastore-emulator")

        public object CloudFirestoreEmulator : SetupGcloudV0.Component("cloud-firestore-emulator")

        public object CloudSpannerEmulator : SetupGcloudV0.Component("cloud-spanner-emulator")

        public object CloudSqlProxy : SetupGcloudV0.Component("cloud_sql_proxy")

        public object ConfigConnector : SetupGcloudV0.Component("config-connector")

        public object Core : SetupGcloudV0.Component("core")

        public object Datalab : SetupGcloudV0.Component("datalab")

        public object DockerCredentialGcr : SetupGcloudV0.Component("docker-credential-gcr")

        public object Gsutil : SetupGcloudV0.Component("gsutil")

        public object Kpt : SetupGcloudV0.Component("kpt")

        public object Kubectl : SetupGcloudV0.Component("kubectl")

        public object KubectlOidc : SetupGcloudV0.Component("kubectl-oidc")

        public object Kustomize : SetupGcloudV0.Component("kustomize")

        public object LocalExtract : SetupGcloudV0.Component("local-extract")

        public object Minikube : SetupGcloudV0.Component("minikube")

        public object Nomos : SetupGcloudV0.Component("nomos")

        public object Pkg : SetupGcloudV0.Component("pkg")

        public object PubsubEmulator : SetupGcloudV0.Component("pubsub-emulator")

        public object Skaffold : SetupGcloudV0.Component("skaffold")

        public class Custom(
            customStringValue: String,
        ) : SetupGcloudV0.Component(customStringValue)
    }
}
