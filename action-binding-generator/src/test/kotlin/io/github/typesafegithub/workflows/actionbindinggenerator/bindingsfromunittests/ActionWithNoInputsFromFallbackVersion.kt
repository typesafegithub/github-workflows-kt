// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.ExposedCopyVisibility
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map

/**
 * Action: Action With No Inputs From Fallback Version
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs-from-fallback-version)
 *
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@Deprecated("These typings were create from a fallback version in https://github.com/typesafegithub/github-actions-typing-catalog, as soon as typings for this version are added, there could be breaking changes, and you need to delete these typings from your local Maven cache typically found in ~/.m2/repository/ to get the updated typing. Consider contributing updated typings to the catalog before using this version.")
@ExposedCopyVisibility
public data class ActionWithNoInputsFromFallbackVersion private constructor(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-no-inputs-from-fallback-version", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(_customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(_customInputs)

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
