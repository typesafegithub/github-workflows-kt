// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.krzema12

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: GitHub Actions Typing
 *
 * Bring type-safety to your GitHub actions' API!
 *
 * [Action on GitHub](https://github.com/krzema12/github-actions-typing)
 */
public class GithubActionsTypingV0(
    /**
     * Set to true to display debug information helpful when troubleshooting issues with this
     * action.
     */
    public val verbose: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("krzema12", "github-actions-typing", _customVersion ?: "v0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            verbose?.let { "verbose" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
