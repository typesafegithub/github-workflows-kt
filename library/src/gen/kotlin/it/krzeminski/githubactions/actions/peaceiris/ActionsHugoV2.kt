// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.peaceiris

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
 */
public data class ActionsHugoV2(
    /**
     * The Hugo version to download (if necessary) and use. Example: 0.58.2
     */
    public val hugoVersion: String? = null,
    /**
     * Download (if necessary) and use Hugo extended version. Example: true
     */
    public val extended: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("peaceiris", "actions-hugo", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            hugoVersion?.let { "hugo-version" to it },
            extended?.let { "extended" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
