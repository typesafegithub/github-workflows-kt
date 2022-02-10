// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * https://github.com/john-smith/action-with-some-optional-inputs
 */
public class ActionWithSomeOptionalInputsV3(
    /**
     * Short description
     */
    public val fooBar: String? = null,
    /**
     * Just another input
     */
    public val bazGoo: String
) : Action("john-smith", "action-with-some-optional-inputs", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            "baz-goo" to bazGoo,
        ).toTypedArray()
    )
}
