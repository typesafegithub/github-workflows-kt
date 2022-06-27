// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.axelop

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Google Java Format
 *
 * Automatically format Java files using Google Java Style
 *
 * [Action on GitHub](https://github.com/axel-op/googlejavaformat-action)
 */
public class GooglejavaformatActionV3(
    /**
     * Arguments for the Google Java Format executable
     */
    public val args: String? = null,
    /**
     * Pattern to match the files to be formatted
     */
    public val files: String? = null,
    /**
     * Pattern to match the files to be ignored by this action
     */
    public val filesExcluded: String? = null,
    /**
     * Set to true to not commit the changes
     */
    public val skipCommit: Boolean? = null,
    /**
     * Version of Google Java Format to use
     */
    public val version: String? = null,
    /**
     * Recommended on MacOS machines
     */
    public val githubToken: String? = null,
    /**
     * Commit message
     */
    public val commitMessage: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("axel-op", "googlejavaformat-action", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            args?.let { "args" to it },
            files?.let { "files" to it },
            filesExcluded?.let { "files-excluded" to it },
            skipCommit?.let { "skipCommit" to it.toString() },
            version?.let { "version" to it },
            githubToken?.let { "githubToken" to it },
            commitMessage?.let { "commitMessage" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
