// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.krzema12

import it.krzeminski.githubactions.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action: GitHub Actions Typing
 *
 * Bring type-safety to your GitHub actions' API!
 *
 * [Action on GitHub](https://github.com/krzema12/github-actions-typing)
 */
@Deprecated(
    message = "This action has a newer major version: GithubActionsTypingV1",
    replaceWith = ReplaceWith("GithubActionsTypingV1"),
)
public data class GithubActionsTypingV0(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("krzema12", "github-actions-typing", _customVersion ?: "v0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> =
            LinkedHashMap(_customInputs)
}
