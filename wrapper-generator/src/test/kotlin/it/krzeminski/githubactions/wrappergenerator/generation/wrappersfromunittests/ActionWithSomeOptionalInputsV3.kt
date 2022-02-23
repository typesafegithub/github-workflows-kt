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
 * [Action on GitHub](https://github.com/john-smith/action-with-some-optional-inputs)
 */
public class ActionWithSomeOptionalInputsV3(
    /**
     * Required is default, default is set
     */
    public val fooBar: String? = null,
    /**
     * Required is default, default is null
     */
    public val bazGoo: String? = null,
    /**
     * Required is false, default is set
     */
    public val zooDar: String? = null,
    /**
     * Required is false, default is default
     */
    public val cooPoo: String? = null,
    /**
     * Required is true, default is default
     */
    public val `package`: String
) : Action("john-smith", "action-with-some-optional-inputs", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            bazGoo?.let { "baz-goo" to it },
            zooDar?.let { "zoo-dar" to it },
            cooPoo?.let { "coo-poo" to it },
            "package" to `package`,
        ).toTypedArray()
    )
}
