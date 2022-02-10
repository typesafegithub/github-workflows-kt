// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: Download a Build Artifact
 *
 * Download a build artifact that was previously uploaded in the workflow by the upload-artifact
 * action
 *
 * https://github.com/actions/download-artifact
 */
public class DownloadArtifactV2(
    /**
     * Artifact name
     */
    public val name: String,
    /**
     * Destination path
     */
    public val path: String
) : Action("actions", "download-artifact", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "name" to name,
            "path" to path,
        ).toTypedArray()
    )
}
