// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
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
public data class DownloadArtifactV2(
    /**
     * Artifact name
     */
    public val name: String? = null,
    /**
     * Destination path
     */
    public val path: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("actions", "download-artifact", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            path?.let { "path" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
