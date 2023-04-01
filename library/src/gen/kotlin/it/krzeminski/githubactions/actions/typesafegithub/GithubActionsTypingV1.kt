// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.typesafegithub

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map

/**
 * Action: GitHub Actions Typing
 *
 * Bring type-safety to your GitHub actions' API!
 *
 * [Action on GitHub](https://github.com/typesafegithub/github-actions-typing)
 */
public data class GithubActionsTypingV1 private constructor(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("typesafegithub", "github-actions-typing", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(_customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> =
            LinkedHashMap(_customInputs)

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
