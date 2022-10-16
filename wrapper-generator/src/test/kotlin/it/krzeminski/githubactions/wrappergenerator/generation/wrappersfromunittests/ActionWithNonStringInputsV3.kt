// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-non-string-inputs)
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
    public val finBin: ActionWithNonStringInputsV3.Bin,
    /**
     * Integer with special value
     */
    public val gooZen: ActionWithNonStringInputsV3.Zen,
    /**
     * Enum with custom naming
     */
    public val bahEnum: ActionWithNonStringInputsV3.Bah,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("john-smith", "action-with-non-string-inputs", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            "baz-goo" to bazGoo.toString(),
            binKin?.let { "bin-kin" to it.toString() },
            "int-pint" to intPint.toString(),
            "boo-zoo" to booZoo.joinToString(","),
            "fin-bin" to finBin.stringValue,
            "goo-zen" to gooZen.integerValue.toString(),
            "bah-enum" to bahEnum.stringValue,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class Bin(
        public val stringValue: String,
    ) {
        public object Foo : ActionWithNonStringInputsV3.Bin("foo")

        public object BooBar : ActionWithNonStringInputsV3.Bin("boo-bar")

        public object Baz123 : ActionWithNonStringInputsV3.Bin("baz123")

        public class Custom(
            customStringValue: String,
        ) : ActionWithNonStringInputsV3.Bin(customStringValue)
    }

    public sealed class Zen(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithNonStringInputsV3.Zen(requestedValue)

        public object Special1 : ActionWithNonStringInputsV3.Zen(3)

        public object Special2 : ActionWithNonStringInputsV3.Zen(-1)
    }

    public sealed class Bah(
        public val stringValue: String,
    ) {
        public object HelloWorld : ActionWithNonStringInputsV3.Bah("helloworld")

        public class Custom(
            customStringValue: String,
        ) : ActionWithNonStringInputsV3.Bah(customStringValue)
    }
}
