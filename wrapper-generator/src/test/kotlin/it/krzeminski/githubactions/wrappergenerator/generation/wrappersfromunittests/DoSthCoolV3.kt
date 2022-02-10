// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import kotlin.String

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * https://github.com/john-smith/do-sth-cool
 */
public class DoSthCoolV3(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * Just another input
     */
    public val bazGoo: String
) : Action("john-smith", "do-sth-cool", "v3") {
    public override fun toYamlArguments() = linkedMapOf(
        "foo-bar" to fooBar,
        "baz-goo" to bazGoo,
    )
}
