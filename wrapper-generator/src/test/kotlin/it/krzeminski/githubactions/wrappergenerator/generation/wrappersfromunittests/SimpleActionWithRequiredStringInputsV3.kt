// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/simple-action-with-required-string-inputs)
 */
public class SimpleActionWithRequiredStringInputsV3(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * Just another input
     */
    @Deprecated("this is deprecated")
    public val bazGoo: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null
) : Action("john-smith", "simple-action-with-required-string-inputs", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            "baz-goo" to bazGoo,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
