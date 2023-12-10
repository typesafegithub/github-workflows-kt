// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Download a Build Artifact
 *
 * Download a build artifact that was previously uploaded in the workflow by the upload-artifact
 * action
 *
 * [Action on GitHub](https://github.com/actions/download-artifact)
 */
@Deprecated(
    message = "This action has a newer major version: DownloadArtifactV3",
    replaceWith = ReplaceWith("DownloadArtifactV3"),
)
public data class DownloadArtifactV2 private constructor(
    /**
     * Artifact name
     */
    public val name: String? = null,
    /**
     * Destination path
     */
    public val path: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions", "download-artifact", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        path: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(name=name, path=path, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            path?.let { "path" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
