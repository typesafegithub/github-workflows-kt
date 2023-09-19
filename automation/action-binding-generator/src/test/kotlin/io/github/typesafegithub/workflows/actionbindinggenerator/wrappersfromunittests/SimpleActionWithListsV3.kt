// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/simple-action-with-lists)
 */
public data class SimpleActionWithListsV3 private constructor(
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
    public val listIntSpecial: List<SimpleActionWithListsV3.MyInt>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "simple-action-with-lists", _customVersion ?: "v3")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        listStrings: List<String>? = null,
        listInts: List<Int>? = null,
        listEnums: List<SimpleActionWithListsV3.MyEnum>? = null,
        listIntSpecial: List<SimpleActionWithListsV3.MyInt>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(listStrings=listStrings, listInts=listInts, listEnums=listEnums,
            listIntSpecial=listIntSpecial, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            listStrings?.let { "list-strings" to it.joinToString(",") },
            listInts?.let { "list-ints" to it.joinToString(",") { it.toString() } },
            listEnums?.let { "list-enums" to it.joinToString(",") { it.stringValue } },
            listIntSpecial?.let { "list-int-special" to it.joinToString(",") {
                    it.integerValue.toString() } },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class MyEnum(
        public val stringValue: String,
    ) {
        public object One : SimpleActionWithListsV3.MyEnum("one")

        public object Two : SimpleActionWithListsV3.MyEnum("two")

        public object Three : SimpleActionWithListsV3.MyEnum("three")

        public class Custom(
            customStringValue: String,
        ) : SimpleActionWithListsV3.MyEnum(customStringValue)
    }

    public sealed class MyInt(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : SimpleActionWithListsV3.MyInt(requestedValue)

        public object TheAnswer : SimpleActionWithListsV3.MyInt(42)
    }
}
