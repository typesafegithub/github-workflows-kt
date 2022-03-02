// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/simple-action-with-lists)
 */
public class SimpleActionWithListsV3(
    /**
     * List of strings
     */
    public val listStrings: List<String>? = null,
    /**
     * List of integers
     */
    public val listInts: List<Int>? = null,
    /**
     * List of enums
     */
    public val listEnums: List<SimpleActionWithListsV3.MyEnum>? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecial: List<SimpleActionWithListsV3.MyInt>? = null
) : Action("john-smith", "simple-action-with-lists", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            listStrings?.let { "list-strings" to it.joinToString(",") },
            listInts?.let { "list-ints" to it.joinToString(",") { it.toString() } },
            listEnums?.let { "list-enums" to it.joinToString(",") { it.stringValue } },
            listIntSpecial?.let { "list-int-special" to it.joinToString(",") {
                    it.integerValue.toString() } },
        ).toTypedArray()
    )

    public sealed class MyEnum(
        public val stringValue: String
    ) {
        public object One : SimpleActionWithListsV3.MyEnum("one")

        public object Two : SimpleActionWithListsV3.MyEnum("two")

        public object Three : SimpleActionWithListsV3.MyEnum("three")

        public class Custom(
            customStringValue: String
        ) : SimpleActionWithListsV3.MyEnum(customStringValue)
    }

    public sealed class MyInt(
        public val integerValue: Int
    ) {
        public class Value(
            requestedValue: Int
        ) : SimpleActionWithListsV3.MyInt(requestedValue)

        public object TheAnswer : SimpleActionWithListsV3.MyInt(42)
    }
}
