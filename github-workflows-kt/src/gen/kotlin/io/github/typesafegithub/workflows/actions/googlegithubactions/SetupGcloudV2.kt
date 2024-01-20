// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.googlegithubactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
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
 * Action: Set up gcloud Cloud SDK environment
 *
 * Downloads, installs, and configures a Google Cloud SDK environment.
 * Adds the `gcloud` CLI command to the $PATH.
 *
 * [Action on GitHub](https://github.com/google-github-actions/setup-gcloud)
 *
 * @param skipInstall Skip installation of the gcloud SDK and use the system-supplied version
 * instead. The "version" input will be ignored.
 * @param version Version or version constraint of the gcloud SDK to install. If
 * unspecified, it will accept any installed version of the gcloud SDK. If
 * set to "latest", it will download the latest available SDK. If set to a
 * version constraint, it will download the latest available version that
 * matches the constraint. Examples: "290.0.1" or ">= 197.0.1".
 * @param projectId ID of the Google Cloud project. If provided, this will configure gcloud to
 * use this project ID by default for commands. Individual commands can still
 * override the project using the --project flag which takes precedence.
 * @param installComponents List of Cloud SDK components to install
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class SetupGcloudV2 private constructor(
    /**
     * Skip installation of the gcloud SDK and use the system-supplied version
     * instead. The "version" input will be ignored.
     */
    public val skipInstall: Boolean? = null,
    /**
     * Version or version constraint of the gcloud SDK to install. If
     * unspecified, it will accept any installed version of the gcloud SDK. If
     * set to "latest", it will download the latest available SDK. If set to a
     * version constraint, it will download the latest available version that
     * matches the constraint. Examples: "290.0.1" or ">= 197.0.1".
     */
    public val version: String? = null,
    /**
     * ID of the Google Cloud project. If provided, this will configure gcloud to
     * use this project ID by default for commands. Individual commands can still
     * override the project using the --project flag which takes precedence.
     */
    public val projectId: String? = null,
    /**
     * List of Cloud SDK components to install
     */
    public val installComponents: List<SetupGcloudV2.Component>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("google-github-actions", "setup-gcloud", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        skipInstall: Boolean? = null,
        version: String? = null,
        projectId: String? = null,
        installComponents: List<SetupGcloudV2.Component>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(skipInstall=skipInstall, version=version, projectId=projectId,
            installComponents=installComponents, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            skipInstall?.let { "skip_install" to it.toString() },
            version?.let { "version" to it },
            projectId?.let { "project_id" to it },
            installComponents?.let { "install_components" to it.joinToString(",") { it.stringValue }
                    },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Component(
        public val stringValue: String,
    ) {
        public object Alpha : SetupGcloudV2.Component("alpha")

        public object AnthosAuth : SetupGcloudV2.Component("anthos-auth")

        public object Appctl : SetupGcloudV2.Component("appctl")

        public object AppEngineGo : SetupGcloudV2.Component("app-engine-go")

        public object AppEngineJava : SetupGcloudV2.Component("app-engine-java")

        public object AppEnginePython : SetupGcloudV2.Component("app-engine-python")

        public object AppEnginePythonExtras : SetupGcloudV2.Component("app-engine-python-extras")

        public object Beta : SetupGcloudV2.Component("beta")

        public object Bigtable : SetupGcloudV2.Component("bigtable")

        public object Bq : SetupGcloudV2.Component("bq")

        public object BundledPython3Unix : SetupGcloudV2.Component("bundled-python3-unix")

        public object Cbt : SetupGcloudV2.Component("cbt")

        public object CloudBuildLocal : SetupGcloudV2.Component("cloud-build-local")

        public object CloudDatastoreEmulator : SetupGcloudV2.Component("cloud-datastore-emulator")

        public object CloudFirestoreEmulator : SetupGcloudV2.Component("cloud-firestore-emulator")

        public object CloudSpannerEmulator : SetupGcloudV2.Component("cloud-spanner-emulator")

        public object CloudSqlProxy : SetupGcloudV2.Component("cloud_sql_proxy")

        public object ConfigConnector : SetupGcloudV2.Component("config-connector")

        public object Core : SetupGcloudV2.Component("core")

        public object Datalab : SetupGcloudV2.Component("datalab")

        public object DockerCredentialGcr : SetupGcloudV2.Component("docker-credential-gcr")

        public object Gsutil : SetupGcloudV2.Component("gsutil")

        public object Kpt : SetupGcloudV2.Component("kpt")

        public object Kubectl : SetupGcloudV2.Component("kubectl")

        public object KubectlOidc : SetupGcloudV2.Component("kubectl-oidc")

        public object Kustomize : SetupGcloudV2.Component("kustomize")

        public object LocalExtract : SetupGcloudV2.Component("local-extract")

        public object Minikube : SetupGcloudV2.Component("minikube")

        public object Nomos : SetupGcloudV2.Component("nomos")

        public object Pkg : SetupGcloudV2.Component("pkg")

        public object PubsubEmulator : SetupGcloudV2.Component("pubsub-emulator")

        public object Skaffold : SetupGcloudV2.Component("skaffold")

        public class Custom(
            customStringValue: String,
        ) : SetupGcloudV2.Component(customStringValue)
    }
}
