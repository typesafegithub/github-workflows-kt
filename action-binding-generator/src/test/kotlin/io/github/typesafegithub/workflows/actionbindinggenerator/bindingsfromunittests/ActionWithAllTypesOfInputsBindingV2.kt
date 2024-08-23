// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.Expression
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Any
import kotlin.Boolean
import kotlin.Deprecated
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
 * [Action on GitHub](https://github.com/john-smith/action-with-all-types-of-inputs-binding-v2)
 *
 * @param fooBar &lt;required&gt; Short description
 * @param fooBar_Untyped &lt;required&gt; Short description
 * @param fooBarExpression &lt;required&gt; Short description
 * @param bazGoo &lt;required&gt; First boolean input!
 * @param bazGoo_Untyped &lt;required&gt; First boolean input!
 * @param bazGooExpression &lt;required&gt; First boolean input!
 * @param binKin Boolean and nullable
 * @param binKin_Untyped Boolean and nullable
 * @param binKinExpression Boolean and nullable
 * @param intPint &lt;required&gt; Integer
 * @param intPint_Untyped &lt;required&gt; Integer
 * @param intPintExpression &lt;required&gt; Integer
 * @param floPint &lt;required&gt; Float
 * @param floPint_Untyped &lt;required&gt; Float
 * @param floPintExpression &lt;required&gt; Float
 * @param finBin &lt;required&gt; Enumeration
 * @param finBin_Untyped &lt;required&gt; Enumeration
 * @param finBinExpression &lt;required&gt; Enumeration
 * @param gooZen &lt;required&gt; Integer with special value
 * @param gooZen_Untyped &lt;required&gt; Integer with special value
 * @param gooZenExpression &lt;required&gt; Integer with special value
 * @param bahEnum &lt;required&gt; Enum with custom naming
 * @param bahEnum_Untyped &lt;required&gt; Enum with custom naming
 * @param bahEnumExpression &lt;required&gt; Enum with custom naming
 * @param listStrings List of strings
 * @param listStrings_Untyped List of strings
 * @param listStringsExpression List of strings
 * @param listStringsExpressions List of strings
 * @param listInts List of integers
 * @param listInts_Untyped List of integers
 * @param listIntsExpression List of integers
 * @param listIntsExpressions List of integers
 * @param listEnums List of enums
 * @param listEnums_Untyped List of enums
 * @param listEnumsExpression List of enums
 * @param listEnumsExpressions List of enums
 * @param listIntSpecial List of integer with special values
 * @param listIntSpecial_Untyped List of integer with special values
 * @param listIntSpecialExpression List of integer with special values
 * @param listIntSpecialExpressions List of integer with special values
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithAllTypesOfInputsBindingV2 private constructor(
    /**
     * &lt;required&gt; Short description
     */
    public val fooBar: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    @Deprecated("Use the typed property or expression property instead")
    public val fooBar_Untyped: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    public val fooBarExpression: Expression<String>? = null,
    /**
     * &lt;required&gt; First boolean input!
     */
    public val bazGoo: Boolean? = null,
    /**
     * &lt;required&gt; First boolean input!
     */
    @Deprecated("Use the typed property or expression property instead")
    public val bazGoo_Untyped: String? = null,
    /**
     * &lt;required&gt; First boolean input!
     */
    public val bazGooExpression: Expression<Boolean>? = null,
    /**
     * Boolean and nullable
     */
    public val binKin: Boolean? = null,
    /**
     * Boolean and nullable
     */
    @Deprecated("Use the typed property or expression property instead")
    public val binKin_Untyped: String? = null,
    /**
     * Boolean and nullable
     */
    public val binKinExpression: Expression<Boolean>? = null,
    /**
     * &lt;required&gt; Integer
     */
    public val intPint: Int? = null,
    /**
     * &lt;required&gt; Integer
     */
    @Deprecated("Use the typed property or expression property instead")
    public val intPint_Untyped: String? = null,
    /**
     * &lt;required&gt; Integer
     */
    public val intPintExpression: Expression<Int>? = null,
    /**
     * &lt;required&gt; Float
     */
    public val floPint: Float? = null,
    /**
     * &lt;required&gt; Float
     */
    @Deprecated("Use the typed property or expression property instead")
    public val floPint_Untyped: String? = null,
    /**
     * &lt;required&gt; Float
     */
    public val floPintExpression: Expression<Float>? = null,
    /**
     * &lt;required&gt; Enumeration
     */
    public val finBin: ActionWithAllTypesOfInputsBindingV2.Bin? = null,
    /**
     * &lt;required&gt; Enumeration
     */
    @Deprecated("Use the typed property or expression property instead")
    public val finBin_Untyped: String? = null,
    /**
     * &lt;required&gt; Enumeration
     */
    public val finBinExpression: Expression<ActionWithAllTypesOfInputsBindingV2.Bin>? = null,
    /**
     * &lt;required&gt; Integer with special value
     */
    public val gooZen: ActionWithAllTypesOfInputsBindingV2.Zen? = null,
    /**
     * &lt;required&gt; Integer with special value
     */
    @Deprecated("Use the typed property or expression property instead")
    public val gooZen_Untyped: String? = null,
    /**
     * &lt;required&gt; Integer with special value
     */
    public val gooZenExpression: Expression<ActionWithAllTypesOfInputsBindingV2.Zen>? = null,
    /**
     * &lt;required&gt; Enum with custom naming
     */
    public val bahEnum: ActionWithAllTypesOfInputsBindingV2.BahEnum? = null,
    /**
     * &lt;required&gt; Enum with custom naming
     */
    @Deprecated("Use the typed property or expression property instead")
    public val bahEnum_Untyped: String? = null,
    /**
     * &lt;required&gt; Enum with custom naming
     */
    public val bahEnumExpression: Expression<ActionWithAllTypesOfInputsBindingV2.BahEnum>? = null,
    /**
     * List of strings
     */
    public val listStrings: List<String>? = null,
    /**
     * List of strings
     */
    @Deprecated("Use the typed property or expression property instead")
    public val listStrings_Untyped: String? = null,
    /**
     * List of strings
     */
    public val listStringsExpression: Expression<List<String>>? = null,
    /**
     * List of strings
     */
    public val listStringsExpressions: List<Expression<String>>? = null,
    /**
     * List of integers
     */
    public val listInts: List<Int>? = null,
    /**
     * List of integers
     */
    @Deprecated("Use the typed property or expression property instead")
    public val listInts_Untyped: String? = null,
    /**
     * List of integers
     */
    public val listIntsExpression: Expression<List<Int>>? = null,
    /**
     * List of integers
     */
    public val listIntsExpressions: List<Expression<Int>>? = null,
    /**
     * List of enums
     */
    public val listEnums: List<ActionWithAllTypesOfInputsBindingV2.MyEnum>? = null,
    /**
     * List of enums
     */
    @Deprecated("Use the typed property or expression property instead")
    public val listEnums_Untyped: String? = null,
    /**
     * List of enums
     */
    public val listEnumsExpression:
            Expression<List<ActionWithAllTypesOfInputsBindingV2.MyEnum>>? = null,
    /**
     * List of enums
     */
    public val listEnumsExpressions:
            List<Expression<ActionWithAllTypesOfInputsBindingV2.MyEnum>>? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecial: List<ActionWithAllTypesOfInputsBindingV2.MyInt>? = null,
    /**
     * List of integer with special values
     */
    @Deprecated("Use the typed property or expression property instead")
    public val listIntSpecial_Untyped: String? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecialExpression:
            Expression<List<ActionWithAllTypesOfInputsBindingV2.MyInt>>? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecialExpressions:
            List<Expression<ActionWithAllTypesOfInputsBindingV2.MyInt>>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<ActionWithAllTypesOfInputsBindingV2.Outputs>("john-smith", "action-with-all-types-of-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(fooBar, fooBar_Untyped, fooBarExpression).size <= 1) {
            "Only one of fooBar, fooBar_Untyped, and fooBarExpression must be set, but not multiple"
        }
        require((fooBar != null) || (fooBar_Untyped != null) || (fooBarExpression != null)) {
            "Either fooBar, fooBar_Untyped, or fooBarExpression must be set, one of them is required"
        }

        require(listOfNotNull(bazGoo, bazGoo_Untyped, bazGooExpression).size <= 1) {
            "Only one of bazGoo, bazGoo_Untyped, and bazGooExpression must be set, but not multiple"
        }
        require((bazGoo != null) || (bazGoo_Untyped != null) || (bazGooExpression != null)) {
            "Either bazGoo, bazGoo_Untyped, or bazGooExpression must be set, one of them is required"
        }

        require(listOfNotNull(binKin, binKin_Untyped, binKinExpression).size <= 1) {
            "Only one of binKin, binKin_Untyped, and binKinExpression must be set, but not multiple"
        }

        require(listOfNotNull(intPint, intPint_Untyped, intPintExpression).size <= 1) {
            "Only one of intPint, intPint_Untyped, and intPintExpression must be set, but not multiple"
        }
        require((intPint != null) || (intPint_Untyped != null) || (intPintExpression != null)) {
            "Either intPint, intPint_Untyped, or intPintExpression must be set, one of them is required"
        }

        require(listOfNotNull(floPint, floPint_Untyped, floPintExpression).size <= 1) {
            "Only one of floPint, floPint_Untyped, and floPintExpression must be set, but not multiple"
        }
        require((floPint != null) || (floPint_Untyped != null) || (floPintExpression != null)) {
            "Either floPint, floPint_Untyped, or floPintExpression must be set, one of them is required"
        }

        require(listOfNotNull(finBin, finBin_Untyped, finBinExpression).size <= 1) {
            "Only one of finBin, finBin_Untyped, and finBinExpression must be set, but not multiple"
        }
        require((finBin != null) || (finBin_Untyped != null) || (finBinExpression != null)) {
            "Either finBin, finBin_Untyped, or finBinExpression must be set, one of them is required"
        }

        require(listOfNotNull(gooZen, gooZen_Untyped, gooZenExpression).size <= 1) {
            "Only one of gooZen, gooZen_Untyped, and gooZenExpression must be set, but not multiple"
        }
        require((gooZen != null) || (gooZen_Untyped != null) || (gooZenExpression != null)) {
            "Either gooZen, gooZen_Untyped, or gooZenExpression must be set, one of them is required"
        }

        require(listOfNotNull(bahEnum, bahEnum_Untyped, bahEnumExpression).size <= 1) {
            "Only one of bahEnum, bahEnum_Untyped, and bahEnumExpression must be set, but not multiple"
        }
        require((bahEnum != null) || (bahEnum_Untyped != null) || (bahEnumExpression != null)) {
            "Either bahEnum, bahEnum_Untyped, or bahEnumExpression must be set, one of them is required"
        }

        require(listOfNotNull(listStrings, listStrings_Untyped, listStringsExpression, listStringsExpressions).size <= 1) {
            "Only one of listStrings, listStrings_Untyped, listStringsExpression, and listStringsExpressions must be set, but not multiple"
        }

        require(listOfNotNull(listInts, listInts_Untyped, listIntsExpression, listIntsExpressions).size <= 1) {
            "Only one of listInts, listInts_Untyped, listIntsExpression, and listIntsExpressions must be set, but not multiple"
        }

        require(listOfNotNull(listEnums, listEnums_Untyped, listEnumsExpression, listEnumsExpressions).size <= 1) {
            "Only one of listEnums, listEnums_Untyped, listEnumsExpression, and listEnumsExpressions must be set, but not multiple"
        }

        require(listOfNotNull(listIntSpecial, listIntSpecial_Untyped, listIntSpecialExpression, listIntSpecialExpressions).size <= 1) {
            "Only one of listIntSpecial, listIntSpecial_Untyped, listIntSpecialExpression, and listIntSpecialExpressions must be set, but not multiple"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = null,
        fooBar_Untyped: String? = null,
        fooBarExpression: Expression<String>? = null,
        bazGoo: Boolean? = null,
        bazGoo_Untyped: String? = null,
        bazGooExpression: Expression<Boolean>? = null,
        binKin: Boolean? = null,
        binKin_Untyped: String? = null,
        binKinExpression: Expression<Boolean>? = null,
        intPint: Int? = null,
        intPint_Untyped: String? = null,
        intPintExpression: Expression<Int>? = null,
        floPint: Float? = null,
        floPint_Untyped: String? = null,
        floPintExpression: Expression<Float>? = null,
        finBin: ActionWithAllTypesOfInputsBindingV2.Bin? = null,
        finBin_Untyped: String? = null,
        finBinExpression: Expression<ActionWithAllTypesOfInputsBindingV2.Bin>? = null,
        gooZen: ActionWithAllTypesOfInputsBindingV2.Zen? = null,
        gooZen_Untyped: String? = null,
        gooZenExpression: Expression<ActionWithAllTypesOfInputsBindingV2.Zen>? = null,
        bahEnum: ActionWithAllTypesOfInputsBindingV2.BahEnum? = null,
        bahEnum_Untyped: String? = null,
        bahEnumExpression: Expression<ActionWithAllTypesOfInputsBindingV2.BahEnum>? = null,
        listStrings: List<String>? = null,
        listStrings_Untyped: String? = null,
        listStringsExpression: Expression<List<String>>? = null,
        listStringsExpressions: List<Expression<String>>? = null,
        listInts: List<Int>? = null,
        listInts_Untyped: String? = null,
        listIntsExpression: Expression<List<Int>>? = null,
        listIntsExpressions: List<Expression<Int>>? = null,
        listEnums: List<ActionWithAllTypesOfInputsBindingV2.MyEnum>? = null,
        listEnums_Untyped: String? = null,
        listEnumsExpression: Expression<List<ActionWithAllTypesOfInputsBindingV2.MyEnum>>? = null,
        listEnumsExpressions: List<Expression<ActionWithAllTypesOfInputsBindingV2.MyEnum>>? = null,
        listIntSpecial: List<ActionWithAllTypesOfInputsBindingV2.MyInt>? = null,
        listIntSpecial_Untyped: String? = null,
        listIntSpecialExpression: Expression<List<ActionWithAllTypesOfInputsBindingV2.MyInt>>? = null,
        listIntSpecialExpressions: List<Expression<ActionWithAllTypesOfInputsBindingV2.MyInt>>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar = fooBar, fooBar_Untyped = fooBar_Untyped, fooBarExpression = fooBarExpression, bazGoo = bazGoo, bazGoo_Untyped = bazGoo_Untyped, bazGooExpression = bazGooExpression, binKin = binKin, binKin_Untyped = binKin_Untyped, binKinExpression = binKinExpression, intPint = intPint, intPint_Untyped = intPint_Untyped, intPintExpression = intPintExpression, floPint = floPint, floPint_Untyped = floPint_Untyped, floPintExpression = floPintExpression, finBin = finBin, finBin_Untyped = finBin_Untyped, finBinExpression = finBinExpression, gooZen = gooZen, gooZen_Untyped = gooZen_Untyped, gooZenExpression = gooZenExpression, bahEnum = bahEnum, bahEnum_Untyped = bahEnum_Untyped, bahEnumExpression = bahEnumExpression, listStrings = listStrings, listStrings_Untyped = listStrings_Untyped, listStringsExpression = listStringsExpression, listStringsExpressions = listStringsExpressions, listInts = listInts, listInts_Untyped = listInts_Untyped, listIntsExpression = listIntsExpression, listIntsExpressions = listIntsExpressions, listEnums = listEnums, listEnums_Untyped = listEnums_Untyped, listEnumsExpression = listEnumsExpression, listEnumsExpressions = listEnumsExpressions, listIntSpecial = listIntSpecial, listIntSpecial_Untyped = listIntSpecial_Untyped, listIntSpecialExpression = listIntSpecialExpression, listIntSpecialExpressions = listIntSpecialExpressions, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            fooBar_Untyped?.let { "foo-bar" to it },
            fooBarExpression?.let { "foo-bar" to it.expressionString },
            bazGoo?.let { "baz-goo" to it.toString() },
            bazGoo_Untyped?.let { "baz-goo" to it },
            bazGooExpression?.let { "baz-goo" to it.expressionString },
            binKin?.let { "bin-kin" to it.toString() },
            binKin_Untyped?.let { "bin-kin" to it },
            binKinExpression?.let { "bin-kin" to it.expressionString },
            intPint?.let { "int-pint" to it.toString() },
            intPint_Untyped?.let { "int-pint" to it },
            intPintExpression?.let { "int-pint" to it.expressionString },
            floPint?.let { "flo-pint" to it.toString() },
            floPint_Untyped?.let { "flo-pint" to it },
            floPintExpression?.let { "flo-pint" to it.expressionString },
            finBin?.let { "fin-bin" to it.stringValue },
            finBin_Untyped?.let { "fin-bin" to it },
            finBinExpression?.let { "fin-bin" to it.expressionString },
            gooZen?.let { "goo-zen" to it.integerValue.toString() },
            gooZen_Untyped?.let { "goo-zen" to it },
            gooZenExpression?.let { "goo-zen" to it.expressionString },
            bahEnum?.let { "bah-enum" to it.stringValue },
            bahEnum_Untyped?.let { "bah-enum" to it },
            bahEnumExpression?.let { "bah-enum" to it.expressionString },
            listStrings?.let { "list-strings" to it.joinToString(",") },
            listStrings_Untyped?.let { "list-strings" to it },
            listStringsExpression?.let { "list-strings" to it.expressionString },
            listStringsExpressions?.let { "list-strings" to it.joinToString(" ", transform = Expression<*>::expressionString) },
            listInts?.let { "list-ints" to it.joinToString(",") { it.toString() } },
            listInts_Untyped?.let { "list-ints" to it },
            listIntsExpression?.let { "list-ints" to it.expressionString },
            listIntsExpressions?.let { "list-ints" to it.joinToString(" ", transform = Expression<*>::expressionString) },
            listEnums?.let { "list-enums" to it.joinToString(",") { it.stringValue } },
            listEnums_Untyped?.let { "list-enums" to it },
            listEnumsExpression?.let { "list-enums" to it.expressionString },
            listEnumsExpressions?.let { "list-enums" to it.joinToString(" ", transform = Expression<*>::expressionString) },
            listIntSpecial?.let { "list-int-special" to it.joinToString(",") { it.integerValue.toString() } },
            listIntSpecial_Untyped?.let { "list-int-special" to it },
            listIntSpecialExpression?.let { "list-int-special" to it.expressionString },
            listIntSpecialExpressions?.let { "list-int-special" to it.joinToString(" ", transform = Expression<*>::expressionString) },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Bin(
        public val stringValue: String,
    ) {
        public object Foo : ActionWithAllTypesOfInputsBindingV2.Bin("foo")

        public object BooBar : ActionWithAllTypesOfInputsBindingV2.Bin("boo-bar")

        public object Baz123 : ActionWithAllTypesOfInputsBindingV2.Bin("baz123")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV2.Bin(customStringValue)
    }

    public sealed class Zen(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithAllTypesOfInputsBindingV2.Zen(requestedValue)

        public object Special1 : ActionWithAllTypesOfInputsBindingV2.Zen(3)

        public object Special2 : ActionWithAllTypesOfInputsBindingV2.Zen(-1)
    }

    public sealed class BahEnum(
        public val stringValue: String,
    ) {
        public object HelloWorld : ActionWithAllTypesOfInputsBindingV2.BahEnum("helloworld")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV2.BahEnum(customStringValue)
    }

    public sealed class MyEnum(
        public val stringValue: String,
    ) {
        public object One : ActionWithAllTypesOfInputsBindingV2.MyEnum("one")

        public object Two : ActionWithAllTypesOfInputsBindingV2.MyEnum("two")

        public object Three : ActionWithAllTypesOfInputsBindingV2.MyEnum("three")

        public class Custom(
            customStringValue: String,
        ) : ActionWithAllTypesOfInputsBindingV2.MyEnum(customStringValue)
    }

    public sealed class MyInt(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithAllTypesOfInputsBindingV2.MyInt(requestedValue)

        public object TheAnswer : ActionWithAllTypesOfInputsBindingV2.MyInt(42)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Short description output
         */
        public val fooBar: Expression<String> = Expression("steps.$stepId.outputs.foo-bar")

        /**
         * Short description output
         */
        public val fooBar_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.foo-bar")

        /**
         * First boolean input! output
         */
        public val bazGoo: Expression<Boolean> = Expression("steps.$stepId.outputs.baz-goo")

        /**
         * First boolean input! output
         */
        public val bazGoo_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.baz-goo")

        /**
         * Boolean and nullable output
         */
        public val binKin: Expression<Boolean> = Expression("steps.$stepId.outputs.bin-kin")

        /**
         * Boolean and nullable output
         */
        public val binKin_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.bin-kin")

        /**
         * Integer output
         */
        public val intPint: Expression<Int> = Expression("steps.$stepId.outputs.int-pint")

        /**
         * Integer output
         */
        public val intPint_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.int-pint")

        /**
         * Float output
         */
        public val floPint: Expression<Float> = Expression("steps.$stepId.outputs.flo-pint")

        /**
         * Float output
         */
        public val floPint_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.flo-pint")

        /**
         * Enumeration output
         */
        public val finBin: Expression<ActionWithAllTypesOfInputsBindingV2.Bin> =
                Expression("steps.$stepId.outputs.fin-bin")

        /**
         * Enumeration output
         */
        public val finBin_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.fin-bin")

        /**
         * Integer with special value output
         */
        public val gooZen: Expression<ActionWithAllTypesOfInputsBindingV2.Zen> =
                Expression("steps.$stepId.outputs.goo-zen")

        /**
         * Integer with special value output
         */
        public val gooZen_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.goo-zen")

        /**
         * Enum with custom naming output
         */
        public val bahEnum: Expression<ActionWithAllTypesOfInputsBindingV2.BahEnum> =
                Expression("steps.$stepId.outputs.bah-enum")

        /**
         * Enum with custom naming output
         */
        public val bahEnum_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.bah-enum")

        /**
         * List of strings output
         */
        public val listStrings: Expression<List<String>> =
                Expression("steps.$stepId.outputs.list-strings")

        /**
         * List of strings output
         */
        public val listStrings_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-strings")

        /**
         * List of integers output
         */
        public val listInts: Expression<List<Int>> = Expression("steps.$stepId.outputs.list-ints")

        /**
         * List of integers output
         */
        public val listInts_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.list-ints")

        /**
         * List of enums output
         */
        public val listEnums: Expression<List<ActionWithAllTypesOfInputsBindingV2.MyEnum>> =
                Expression("steps.$stepId.outputs.list-enums")

        /**
         * List of enums output
         */
        public val listEnums_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-enums")

        /**
         * List of integer with special values output
         */
        public val listIntSpecial: Expression<List<ActionWithAllTypesOfInputsBindingV2.MyInt>> =
                Expression("steps.$stepId.outputs.list-int-special")

        /**
         * List of integer with special values output
         */
        public val listIntSpecial_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-int-special")
    }
}
