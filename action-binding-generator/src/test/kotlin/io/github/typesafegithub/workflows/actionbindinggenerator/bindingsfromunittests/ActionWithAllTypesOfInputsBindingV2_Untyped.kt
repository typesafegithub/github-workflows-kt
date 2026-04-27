// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.Expression
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Any
import kotlin.Deprecated
import kotlin.ExposedCopyVisibility
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * ```text
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!                             WARNING                             !!!
 * !!!                                                                 !!!
 * !!! This action binding has no typings provided. All inputs will    !!!
 * !!! have a default type of String.                                  !!!
 * !!! To be able to use this action in a type-safe way, ask the       !!!
 * !!! action's owner to provide the typings using                     !!!
 * !!!                                                                 !!!
 * !!! https://github.com/typesafegithub/github-actions-typing         !!!
 * !!!                                                                 !!!
 * !!! or if it's impossible, contribute typings to a community-driven !!!
 * !!!                                                                 !!!
 * !!! https://github.com/typesafegithub/github-actions-typing-catalog !!!
 * !!!                                                                 !!!
 * !!! This '_Untyped' binding will be available even once the typings !!!
 * !!! are added.                                                      !!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * ```
 *
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-all-types-of-inputs-binding-v2)
 *
 * @param fooBar_Untyped Short description
 * @param fooBarExpression Short description
 * @param bazGoo_Untyped First boolean input!
 * @param bazGooExpression First boolean input!
 * @param binKin_Untyped Boolean and nullable
 * @param binKinExpression Boolean and nullable
 * @param intPint_Untyped Integer
 * @param intPintExpression Integer
 * @param floPint_Untyped Float
 * @param floPintExpression Float
 * @param finBin_Untyped Enumeration
 * @param finBinExpression Enumeration
 * @param gooZen_Untyped Integer with special value
 * @param gooZenExpression Integer with special value
 * @param bahEnum_Untyped Enum with custom naming
 * @param bahEnumExpression Enum with custom naming
 * @param listStrings_Untyped List of strings
 * @param listStringsExpression List of strings
 * @param listInts_Untyped List of integers
 * @param listIntsExpression List of integers
 * @param listEnums_Untyped List of enums
 * @param listEnumsExpression List of enums
 * @param listIntSpecial_Untyped List of integer with special values
 * @param listIntSpecialExpression List of integer with special values
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    "Use the typed class instead",
    ReplaceWith("ActionWithAllTypesOfInputsBindingV2"),
)
@ExposedCopyVisibility
public data class ActionWithAllTypesOfInputsBindingV2_Untyped private constructor(
    /**
     * Short description
     */
    public val fooBar_Untyped: String? = null,
    /**
     * Short description
     */
    public val fooBarExpression: Expression<String>? = null,
    /**
     * First boolean input!
     */
    public val bazGoo_Untyped: String? = null,
    /**
     * First boolean input!
     */
    public val bazGooExpression: Expression<String>? = null,
    /**
     * Boolean and nullable
     */
    public val binKin_Untyped: String? = null,
    /**
     * Boolean and nullable
     */
    public val binKinExpression: Expression<String>? = null,
    /**
     * Integer
     */
    public val intPint_Untyped: String? = null,
    /**
     * Integer
     */
    public val intPintExpression: Expression<String>? = null,
    /**
     * Float
     */
    public val floPint_Untyped: String? = null,
    /**
     * Float
     */
    public val floPintExpression: Expression<String>? = null,
    /**
     * Enumeration
     */
    public val finBin_Untyped: String? = null,
    /**
     * Enumeration
     */
    public val finBinExpression: Expression<String>? = null,
    /**
     * Integer with special value
     */
    public val gooZen_Untyped: String? = null,
    /**
     * Integer with special value
     */
    public val gooZenExpression: Expression<String>? = null,
    /**
     * Enum with custom naming
     */
    public val bahEnum_Untyped: String? = null,
    /**
     * Enum with custom naming
     */
    public val bahEnumExpression: Expression<String>? = null,
    /**
     * List of strings
     */
    public val listStrings_Untyped: String? = null,
    /**
     * List of strings
     */
    public val listStringsExpression: Expression<String>? = null,
    /**
     * List of integers
     */
    public val listInts_Untyped: String? = null,
    /**
     * List of integers
     */
    public val listIntsExpression: Expression<String>? = null,
    /**
     * List of enums
     */
    public val listEnums_Untyped: String? = null,
    /**
     * List of enums
     */
    public val listEnumsExpression: Expression<String>? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecial_Untyped: String? = null,
    /**
     * List of integer with special values
     */
    public val listIntSpecialExpression: Expression<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<ActionWithAllTypesOfInputsBindingV2_Untyped.Outputs>("john-smith", "action-with-all-types-of-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(fooBar_Untyped, fooBarExpression).size <= 1) {
            "Only one of fooBar_Untyped, and fooBarExpression must be set, but not multiple"
        }
        require((fooBar_Untyped != null) || (fooBarExpression != null)) {
            "Either fooBar_Untyped, or fooBarExpression must be set, one of them is required"
        }

        require(listOfNotNull(bazGoo_Untyped, bazGooExpression).size <= 1) {
            "Only one of bazGoo_Untyped, and bazGooExpression must be set, but not multiple"
        }
        require((bazGoo_Untyped != null) || (bazGooExpression != null)) {
            "Either bazGoo_Untyped, or bazGooExpression must be set, one of them is required"
        }

        require(listOfNotNull(binKin_Untyped, binKinExpression).size <= 1) {
            "Only one of binKin_Untyped, and binKinExpression must be set, but not multiple"
        }

        require(listOfNotNull(intPint_Untyped, intPintExpression).size <= 1) {
            "Only one of intPint_Untyped, and intPintExpression must be set, but not multiple"
        }
        require((intPint_Untyped != null) || (intPintExpression != null)) {
            "Either intPint_Untyped, or intPintExpression must be set, one of them is required"
        }

        require(listOfNotNull(floPint_Untyped, floPintExpression).size <= 1) {
            "Only one of floPint_Untyped, and floPintExpression must be set, but not multiple"
        }
        require((floPint_Untyped != null) || (floPintExpression != null)) {
            "Either floPint_Untyped, or floPintExpression must be set, one of them is required"
        }

        require(listOfNotNull(finBin_Untyped, finBinExpression).size <= 1) {
            "Only one of finBin_Untyped, and finBinExpression must be set, but not multiple"
        }
        require((finBin_Untyped != null) || (finBinExpression != null)) {
            "Either finBin_Untyped, or finBinExpression must be set, one of them is required"
        }

        require(listOfNotNull(gooZen_Untyped, gooZenExpression).size <= 1) {
            "Only one of gooZen_Untyped, and gooZenExpression must be set, but not multiple"
        }
        require((gooZen_Untyped != null) || (gooZenExpression != null)) {
            "Either gooZen_Untyped, or gooZenExpression must be set, one of them is required"
        }

        require(listOfNotNull(bahEnum_Untyped, bahEnumExpression).size <= 1) {
            "Only one of bahEnum_Untyped, and bahEnumExpression must be set, but not multiple"
        }
        require((bahEnum_Untyped != null) || (bahEnumExpression != null)) {
            "Either bahEnum_Untyped, or bahEnumExpression must be set, one of them is required"
        }

        require(listOfNotNull(listStrings_Untyped, listStringsExpression).size <= 1) {
            "Only one of listStrings_Untyped, and listStringsExpression must be set, but not multiple"
        }

        require(listOfNotNull(listInts_Untyped, listIntsExpression).size <= 1) {
            "Only one of listInts_Untyped, and listIntsExpression must be set, but not multiple"
        }

        require(listOfNotNull(listEnums_Untyped, listEnumsExpression).size <= 1) {
            "Only one of listEnums_Untyped, and listEnumsExpression must be set, but not multiple"
        }

        require(listOfNotNull(listIntSpecial_Untyped, listIntSpecialExpression).size <= 1) {
            "Only one of listIntSpecial_Untyped, and listIntSpecialExpression must be set, but not multiple"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar_Untyped: String? = null,
        fooBarExpression: Expression<String>? = null,
        bazGoo_Untyped: String? = null,
        bazGooExpression: Expression<String>? = null,
        binKin_Untyped: String? = null,
        binKinExpression: Expression<String>? = null,
        intPint_Untyped: String? = null,
        intPintExpression: Expression<String>? = null,
        floPint_Untyped: String? = null,
        floPintExpression: Expression<String>? = null,
        finBin_Untyped: String? = null,
        finBinExpression: Expression<String>? = null,
        gooZen_Untyped: String? = null,
        gooZenExpression: Expression<String>? = null,
        bahEnum_Untyped: String? = null,
        bahEnumExpression: Expression<String>? = null,
        listStrings_Untyped: String? = null,
        listStringsExpression: Expression<String>? = null,
        listInts_Untyped: String? = null,
        listIntsExpression: Expression<String>? = null,
        listEnums_Untyped: String? = null,
        listEnumsExpression: Expression<String>? = null,
        listIntSpecial_Untyped: String? = null,
        listIntSpecialExpression: Expression<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar_Untyped = fooBar_Untyped, fooBarExpression = fooBarExpression, bazGoo_Untyped = bazGoo_Untyped, bazGooExpression = bazGooExpression, binKin_Untyped = binKin_Untyped, binKinExpression = binKinExpression, intPint_Untyped = intPint_Untyped, intPintExpression = intPintExpression, floPint_Untyped = floPint_Untyped, floPintExpression = floPintExpression, finBin_Untyped = finBin_Untyped, finBinExpression = finBinExpression, gooZen_Untyped = gooZen_Untyped, gooZenExpression = gooZenExpression, bahEnum_Untyped = bahEnum_Untyped, bahEnumExpression = bahEnumExpression, listStrings_Untyped = listStrings_Untyped, listStringsExpression = listStringsExpression, listInts_Untyped = listInts_Untyped, listIntsExpression = listIntsExpression, listEnums_Untyped = listEnums_Untyped, listEnumsExpression = listEnumsExpression, listIntSpecial_Untyped = listIntSpecial_Untyped, listIntSpecialExpression = listIntSpecialExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar_Untyped?.let { "foo-bar" to it },
            fooBarExpression?.let { "foo-bar" to it.expressionString },
            bazGoo_Untyped?.let { "baz-goo" to it },
            bazGooExpression?.let { "baz-goo" to it.expressionString },
            binKin_Untyped?.let { "bin-kin" to it },
            binKinExpression?.let { "bin-kin" to it.expressionString },
            intPint_Untyped?.let { "int-pint" to it },
            intPintExpression?.let { "int-pint" to it.expressionString },
            floPint_Untyped?.let { "flo-pint" to it },
            floPintExpression?.let { "flo-pint" to it.expressionString },
            finBin_Untyped?.let { "fin-bin" to it },
            finBinExpression?.let { "fin-bin" to it.expressionString },
            gooZen_Untyped?.let { "goo-zen" to it },
            gooZenExpression?.let { "goo-zen" to it.expressionString },
            bahEnum_Untyped?.let { "bah-enum" to it },
            bahEnumExpression?.let { "bah-enum" to it.expressionString },
            listStrings_Untyped?.let { "list-strings" to it },
            listStringsExpression?.let { "list-strings" to it.expressionString },
            listInts_Untyped?.let { "list-ints" to it },
            listIntsExpression?.let { "list-ints" to it.expressionString },
            listEnums_Untyped?.let { "list-enums" to it },
            listEnumsExpression?.let { "list-enums" to it.expressionString },
            listIntSpecial_Untyped?.let { "list-int-special" to it },
            listIntSpecialExpression?.let { "list-int-special" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Short description output
         */
        public val fooBar_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.foo-bar")

        /**
         * First boolean input! output
         */
        public val bazGoo_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.baz-goo")

        /**
         * Boolean and nullable output
         */
        public val binKin_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.bin-kin")

        /**
         * Integer output
         */
        public val intPint_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.int-pint")

        /**
         * Float output
         */
        public val floPint_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.flo-pint")

        /**
         * Enumeration output
         */
        public val finBin_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.fin-bin")

        /**
         * Integer with special value output
         */
        public val gooZen_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.goo-zen")

        /**
         * Enum with custom naming output
         */
        public val bahEnum_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.bah-enum")

        /**
         * List of strings output
         */
        public val listStrings_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-strings")

        /**
         * List of integers output
         */
        public val listInts_Untyped: Expression<Any> = Expression("steps.$stepId.outputs.list-ints")

        /**
         * List of enums output
         */
        public val listEnums_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-enums")

        /**
         * List of integer with special values output
         */
        public val listIntSpecial_Untyped: Expression<Any> =
                Expression("steps.$stepId.outputs.list-int-special")
    }
}
