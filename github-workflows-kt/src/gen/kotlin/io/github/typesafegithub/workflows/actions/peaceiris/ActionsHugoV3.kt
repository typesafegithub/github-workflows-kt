// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.peaceiris

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Hugo setup
 *
 * GitHub Actions for Hugo ⚡️ Setup Hugo quickly and build your site fast. Hugo extended and Hugo
 * Modules are supported.
 *
 * [Action on GitHub](https://github.com/peaceiris/actions-hugo)
 *
 * @param hugoVersion The Hugo version to download (if necessary) and use. Example: 0.58.2
 * @param extended Download (if necessary) and use Hugo extended version. Example: true
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class ActionsHugoV3 private constructor(
    /**
     * The Hugo version to download (if necessary) and use. Example: 0.58.2
     */
    public val hugoVersion: String? = null,
    /**
     * Download (if necessary) and use Hugo extended version. Example: true
     */
    public val extended: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("peaceiris", "actions-hugo", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        hugoVersion: String? = null,
        extended: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(hugoVersion=hugoVersion, extended=extended, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            hugoVersion?.let { "hugo-version" to it },
            extended?.let { "extended" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
