// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.ExposedCopyVisibility
import kotlin.Float
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
 * [Action on GitHub](https://github.com/john-smith/action-with-all-types-of-inputs-binding-v1)
 *
 * @param fooBar &lt;required&gt; Short description
 * @param fooBar_Untyped &lt;required&gt; Short description
 * @param bazGoo &lt;required&gt; First boolean input!
 * @param bazGoo_Untyped &lt;required&gt; First boolean input!
 * @param binKin Boolean and nullable
 * @param binKin_Untyped Boolean and nullable
 * @param intPint &lt;required&gt; Integer
 * @param intPint_Untyped &lt;required&gt; Integer
 * @param floPint &lt;required&gt; Float
 * @param floPint_Untyped &lt;required&gt; Float
 * @param finBin &lt;required&gt; Enumeration
 * @param finBin_Untyped &lt;required&gt; Enumeration
 * @param gooZen &lt;required&gt; Integer with special value
 * @param gooZen_Untyped &lt;required&gt; Integer with special value
 * @param bahEnum &lt;required&gt; Enum with custom naming
 * @param bahEnum_Untyped &lt;required&gt; Enum with custom naming
 * @param listStrings List of strings
 * @param listStrings_Untyped List of strings
 * @param listInts List of integers
 * @param listInts_Untyped List of integers
 * @param listEnums List of enums
 * @param listEnums_Untyped List of enums
 * @param listIntSpecial List of integer with special values
 * @param listIntSpecial_Untyped List of integer with special values
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithAllTypesOfInputsBindingV1 private constructor(
    /**
     * &lt;required&gt; Short description
     */
    public val fooBar: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    public val fooBar_Untyped: String? = null,
    /**
     * &lt;required&gt; First boolean input!
     */
    public val bazGoo: Boolean? = null,
    /**
     * &lt;required&gt; First boolean input!
     */
    public val bazGoo_Untyped: String? = null,
    /**
     * Boolean and nullable
     */
    public val binKin: Boolean? = null,
    /**
     * Boolean and nullable
     */
    public val binKin_Untyped: String? = null,
    /**
     * &lt;required&gt; Integer
     */
    public val intPint: Int? = null,
    /**
     * &lt;required&gt; Integer
     */
    public val intPint_Untyped: String? = null,
    /**
     * &lt;required&gt; Float
     */
    public val floPint: Float? = null,
    /**
     * &lt;required&gt; Float
     */
    public val floPint_Untyped: String? = null,
    /**
     * &lt;required&gt; Enumeration
     */
    public val finBin: ActionWithAllTypesOfInputsBindingV1.Bin? = null,
    /**
     * &lt;required&gt; Enumeration
     */
    public val finBin_Untyped: String? = null,
    /**
     * &lt;required&gt; Integer with special value
     */
    public val gooZen: ActionWithAllTypesOfInputsBindingV1.Zen? = null,
    /**
     * &lt;required&gt; Integer with special value
     */
    public val gooZen_Untyped: String? = null,
    /**
     * &lt;required&gt; Enum with custom naming
     */
    public val bahEnum: ActionWithAllTypesOfInputsBindingV1.BahEnum? = null,
    /**
     * &lt;required&gt; Enum with custom naming
     */
    public val bahEnum_Untyped: String? = null,
    /**
     * List of strings
     */
    public val listStrings: List<String>? = null,
    /**
     * List of strings
     */
    public val listStrings_Untyped: String? = null,
    /**
     * List of integers
     */
    public val listInts: List<Int>? = null,
    /**
     * List of integers
     */
    public val listInts_Untyped: String? = null,
    /**
     * List of enums
     */
    public val listEnums: List<ActionWithAllTypesOfInputsBindingV1.MyEnum>? = null,
    /**
     * List of enums
     */
    public val listEnums_Untyped: String? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecial: List<ActionWithAllTypesOfInputsBindingV1.MyInt>? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecial_Untyped: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<ActionWithAllTypesOfInputsBindingV1.Outputs>("john-smith", "action-with-all-types-of-inputs-binding-v1", _customVersion ?: "v3") {
    init {
        require(listOfNotNull(fooBar, fooBar_Untyped).size <= 1) {
            "Only one of fooBar, and fooBar_Untyped must be set, but not multiple"
        }
        require((fooBar != null) || (fooBar_Untyped != null)) {
            "Either fooBar, or fooBar_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(bazGoo, bazGoo_Untyped).size <= 1) {
            "Only one of bazGoo, and bazGoo_Untyped must be set, but not multiple"
        }
        require((bazGoo != null) || (bazGoo_Untyped != null)) {
            "Either bazGoo, or bazGoo_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(binKin, binKin_Untyped).size <= 1) {
            "Only one of binKin, and binKin_Untyped must be set, but not multiple"
        }

        require(listOfNotNull(intPint, intPint_Untyped).size <= 1) {
            "Only one of intPint, and intPint_Untyped must be set, but not multiple"
        }
        require((intPint != null) || (intPint_Untyped != null)) {
            "Either intPint, or intPint_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(floPint, floPint_Untyped).size <= 1) {
            "Only one of floPint, and floPint_Untyped must be set, but not multiple"
        }
        require((floPint != null) || (floPint_Untyped != null)) {
            "Either floPint, or floPint_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(finBin, finBin_Untyped).size <= 1) {
            "Only one of finBin, and finBin_Untyped must be set, but not multiple"
        }
        require((finBin != null) || (finBin_Untyped != null)) {
            "Either finBin, or finBin_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(gooZen, gooZen_Untyped).size <= 1) {
            "Only one of gooZen, and gooZen_Untyped must be set, but not multiple"
        }
        require((gooZen != null) || (gooZen_Untyped != null)) {
            "Either gooZen, or gooZen_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(bahEnum, bahEnum_Untyped).size <= 1) {
            "Only one of bahEnum, and bahEnum_Untyped must be set, but not multiple"
        }
        require((bahEnum != null) || (bahEnum_Untyped != null)) {
            "Either bahEnum, or bahEnum_Untyped must be set, one of them is required"
        }

        require(listOfNotNull(listStrings, listStrings_Untyped).size <= 1) {
            "Only one of listStrings, and listStrings_Untyped must be set, but not multiple"
        }

        require(listOfNotNull(listInts, listInts_Untyped).size <= 1) {
            "Only one of listInts, and listInts_Untyped must be set, but not multiple"
        }

        require(listOfNotNull(listEnums, listEnums_Untyped).size <= 1) {
            "Only one of listEnums, and listEnums_Untyped must be set, but not multiple"
        }

        require(listOfNotNull(listIntSpecial, listIntSpecial_Untyped).size <= 1) {
            "Only one of listIntSpecial, and listIntSpecial_Untyped must be set, but not multiple"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = null,
        fooBar_Untyped: String? = null,
        bazGoo: Boolean? = null,
        bazGoo_Untyped: String? = null,
        binKin: Boolean? = null,
        binKin_Untyped: String? = null,
        intPint: Int? = null,
        intPint_Untyped: String? = null,
        floPint: Float? = null,
        floPint_Untyped: String? = null,
        finBin: ActionWithAllTypesOfInputsBindingV1.Bin? = null,
        finBin_Untyped: String? = null,
        gooZen: ActionWithAllTypesOfInputsBindingV1.Zen? = null,
        gooZen_Untyped: String? = null,
        bahEnum: ActionWithAllTypesOfInputsBindingV1.BahEnum? = null,
        bahEnum_Untyped: String? = null,
        listStrings: List<String>? = null,
        listStrings_Untyped: String? = null,
        listInts: List<Int>? = null,
        listInts_Untyped: String? = null,
        listEnums: List<ActionWithAllTypesOfInputsBindingV1.MyEnum>? = null,
        listEnums_Untyped: String? = null,
        listIntSpecial: List<ActionWithAllTypesOfInputsBindingV1.MyInt>? = null,
        listIntSpecial_Untyped: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar = fooBar, fooBar_Untyped = fooBar_Untyped, bazGoo = bazGoo, bazGoo_Untyped = bazGoo_Untyped, binKin = binKin, binKin_Untyped = binKin_Untyped, intPint = intPint, intPint_Untyped = intPint_Untyped, floPint = floPint, floPint_Untyped = floPint_Untyped, finBin = finBin, finBin_Untyped = finBin_Untyped, gooZen = gooZen, gooZen_Untyped = gooZen_Untyped, bahEnum = bahEnum, bahEnum_Untyped = bahEnum_Untyped, listStrings = listStrings, listStrings_Untyped = listStrings_Untyped, listInts = listInts, listInts_Untyped = listInts_Untyped, listEnums = listEnums, listEnums_Untyped = listEnums_Untyped, listIntSpecial = listIntSpecial, listIntSpecial_Untyped = listIntSpecial_Untyped, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            fooBar_Untyped?.let { "foo-bar" to it },
            bazGoo?.let { "baz-goo" to it.toString() },
            bazGoo_Untyped?.let { "baz-goo" to it },
            binKin?.let { "bin-kin" to it.toString() },
            binKin_Untyped?.let { "bin-kin" to it },
            intPint?.let { "int-pint" to it.toString() },
            intPint_Untyped?.let { "int-pint" to it },
            floPint?.let { "flo-pint" to it.toString() },
            floPint_Untyped?.let { "flo-pint" to it },
            finBin?.let { "fin-bin" to it.stringValue },
            finBin_Untyped?.let { "fin-bin" to it },
            gooZen?.let { "goo-zen" to it.integerValue.toString() },
            gooZen_Untyped?.let { "goo-zen" to it },
            bahEnum?.let { "bah-enum" to it.stringValue },
            bahEnum_Untyped?.let { "bah-enum" to it },
            listStrings?.let { "list-strings" to it.joinToString(",") },
            listStrings_Untyped?.let { "list-strings" to it },
            listInts?.let { "list-ints" to it.joinToString(",") { it.toString() } },
            listInts_Untyped?.let { "list-ints" to it },
            listEnums?.let { "list-enums" to it.joinToString(",") { it.stringValue } },
            listEnums_Untyped?.let { "list-enums" to it },
            listIntSpecial?.let { "list-int-special" to it.joinToString(",") { it.integerValue.toString() } },
            listIntSpecial_Untyped?.let { "list-int-special" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Bin(
        public val stringValue: String,
    ) {
        public object Foo : ActionWithAllTypesOfInputsBindingV1.Bin("foo")

        public object BooBar : ActionWithAllTypesOfInputsBindingV1.Bin("boo-bar")

        public object Baz123 : ActionWithAllTypesOfInputsBindingV1.Bin("baz123")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV1.Bin(customStringValue)
    }

    public sealed class Zen(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithAllTypesOfInputsBindingV1.Zen(requestedValue)

        public object Special1 : ActionWithAllTypesOfInputsBindingV1.Zen(3)

        public object Special2 : ActionWithAllTypesOfInputsBindingV1.Zen(-1)
    }

    public sealed class BahEnum(
        public val stringValue: String,
    ) {
        public object HelloWorld : ActionWithAllTypesOfInputsBindingV1.BahEnum("helloworld")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV1.BahEnum(customStringValue)
    }

    public sealed class MyEnum(
        public val stringValue: String,
    ) {
        public object One : ActionWithAllTypesOfInputsBindingV1.MyEnum("one")

        public object Two : ActionWithAllTypesOfInputsBindingV1.MyEnum("two")

        public object Three : ActionWithAllTypesOfInputsBindingV1.MyEnum("three")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV1.MyEnum(customStringValue)
    }

    public sealed class MyInt(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithAllTypesOfInputsBindingV1.MyInt(requestedValue)

        public object TheAnswer : ActionWithAllTypesOfInputsBindingV1.MyInt(42)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Short description output
         */
        public val fooBar: String = "steps.$stepId.outputs.foo-bar"

        /**
         * First boolean input! output
         */
        public val bazGoo: String = "steps.$stepId.outputs.baz-goo"

        /**
         * Boolean and nullable output
         */
        public val binKin: String = "steps.$stepId.outputs.bin-kin"

        /**
         * Integer output
         */
        public val intPint: String = "steps.$stepId.outputs.int-pint"

        /**
         * Float output
         */
        public val floPint: String = "steps.$stepId.outputs.flo-pint"

        /**
         * Enumeration output
         */
        public val finBin: String = "steps.$stepId.outputs.fin-bin"

        /**
         * Integer with special value output
         */
        public val gooZen: String = "steps.$stepId.outputs.goo-zen"

        /**
         * Enum with custom naming output
         */
        public val bahEnum: String = "steps.$stepId.outputs.bah-enum"

        /**
         * List of strings output
         */
        public val listStrings: String = "steps.$stepId.outputs.list-strings"

        /**
         * List of integers output
         */
        public val listInts: String = "steps.$stepId.outputs.list-ints"

        /**
         * List of enums output
         */
        public val listEnums: String = "steps.$stepId.outputs.list-enums"

        /**
         * List of integer with special values output
         */
        public val listIntSpecial: String = "steps.$stepId.outputs.list-int-special"
    }
}
