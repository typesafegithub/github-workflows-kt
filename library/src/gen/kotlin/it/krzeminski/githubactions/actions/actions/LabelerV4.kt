// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Labeler
 *
 * Automatically label new pull requests based on the paths of files being changed
 *
 * [Action on GitHub](https://github.com/actions/labeler)
 */
public class LabelerV4(
    /**
     * The GITHUB_TOKEN secret
     */
    public val repoToken: String? = null,
    /**
     * The path for the label configurations
     */
    public val configurationPath: String? = null,
    /**
     * Whether or not to remove labels when matching files are reverted
     */
    public val syncLabels: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customArguments: Map<String, String> = mapOf()
) : Action("actions", "labeler", "v4") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            repoToken?.let { "repo-token" to it },
            configurationPath?.let { "configuration-path" to it },
            syncLabels?.let { "sync-labels" to it.toString() },
            *_customArguments.toList().toTypedArray(),
        ).toTypedArray()
    )
}
