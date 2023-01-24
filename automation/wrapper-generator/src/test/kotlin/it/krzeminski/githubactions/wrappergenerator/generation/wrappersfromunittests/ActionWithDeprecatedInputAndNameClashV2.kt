// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Some Action
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-deprecated-input-and-name-clash)
 */
public data class ActionWithDeprecatedInputAndNameClashV2(
    /**
     * Foo bar - new
     */
    public val fooBar: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("john-smith", "action-with-deprecated-input-and-name-clash", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "fooBar" to fooBar,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
