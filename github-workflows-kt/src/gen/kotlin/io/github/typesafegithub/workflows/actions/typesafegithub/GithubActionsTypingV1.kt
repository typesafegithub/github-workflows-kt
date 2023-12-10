// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.typesafegithub

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("typesafegithub", "github-actions-typing", _customVersion ?: "v1")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(_customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(_customInputs)

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
