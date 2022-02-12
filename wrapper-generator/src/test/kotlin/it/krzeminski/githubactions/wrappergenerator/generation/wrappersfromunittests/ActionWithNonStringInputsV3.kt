// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * https://github.com/john-smith/action-with-non-string-inputs
 */
public class ActionWithNonStringInputsV3(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * First boolean input!
     */
    public val bazGoo: Boolean,
    /**
     * Boolean and nullable
     */
    public val binKin: Boolean? = null,
    /**
     * Integer
     */
    public val intPint: Int,
    /**
     * List of strings
     */
    public val booZoo: List<String>,
    /**
     * Enumeration
     */
    public val finBin: ActionWithNonStringInputsV3.Bin
) : Action("john-smith", "action-with-non-string-inputs", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            "baz-goo" to bazGoo.toString(),
            binKin?.let { "bin-kin" to it.toString() },
            "int-pint" to intPint.toString(),
            "boo-zoo" to booZoo.joinToString(","),
            "fin-bin" to finBin.stringValue,
        ).toTypedArray()
    )

    public sealed class Bin(
        public val stringValue: String
    ) {
        public object Foo : ActionWithNonStringInputsV3.Bin("foo")

        public object BooBar : ActionWithNonStringInputsV3.Bin("boo-bar")

        public object Baz123 : ActionWithNonStringInputsV3.Bin("baz123")

        public class Custom(
            customStringValue: String
        ) : ActionWithNonStringInputsV3.Bin(customStringValue)
    }
}
