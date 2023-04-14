// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.googlecloudplatform

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
 * Action: Set up gcloud Cloud SDK environment
 *
 * Downloads, installs, and configures a Google Cloud SDK environment.
 * Adds the `gcloud` CLI command to the $PATH.
 *
 * [Action on GitHub](https://github.com/GoogleCloudPlatform/github-actions)
 */
public data class GithubActionsV1 private constructor(
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
    public val installComponents: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("GoogleCloudPlatform", "github-actions", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        skipInstall: Boolean? = null,
        version: String? = null,
        projectId: String? = null,
        installComponents: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(skipInstall=skipInstall, version=version, projectId=projectId,
            installComponents=installComponents, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            skipInstall?.let { "skip_install" to it.toString() },
            version?.let { "version" to it },
            projectId?.let { "project_id" to it },
            installComponents?.let { "install_components" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
