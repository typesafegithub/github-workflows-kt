// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import java.util.Objects
import kotlin.Any
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.Int
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
 * @param bazGoo_Untyped First boolean input!
 * @param binKin_Untyped Boolean and nullable
 * @param intPint_Untyped Integer
 * @param floPint_Untyped Float
 * @param finBin_Untyped Enumeration
 * @param gooZen_Untyped Integer with special value
 * @param bahEnum_Untyped Enum with custom naming
 * @param listStrings_Untyped List of strings
 * @param listInts_Untyped List of integers
 * @param listEnums_Untyped List of enums
 * @param listIntSpecial_Untyped List of integer with special values
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    "Use the typed class instead",
    ReplaceWith("ActionWithAllTypesOfInputsBindingV2"),
)
public class ActionWithAllTypesOfInputsBindingV2_Untyped(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * Short description
     */
    public val fooBar_Untyped: String,
    /**
     * First boolean input!
     */
    public val bazGoo_Untyped: String,
    /**
     * Boolean and nullable
     */
    public val binKin_Untyped: String? = null,
    /**
     * Integer
     */
    public val intPint_Untyped: String,
    /**
     * Float
     */
    public val floPint_Untyped: String,
    /**
     * Enumeration
     */
    public val finBin_Untyped: String,
    /**
     * Integer with special value
     */
    public val gooZen_Untyped: String,
    /**
     * Enum with custom naming
     */
    public val bahEnum_Untyped: String,
    /**
     * List of strings
     */
    public val listStrings_Untyped: String? = null,
    /**
     * List of integers
     */
    public val listInts_Untyped: String? = null,
    /**
     * List of enums
     */
    public val listEnums_Untyped: String? = null,
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
) : RegularAction<ActionWithAllTypesOfInputsBindingV2_Untyped.Outputs>("john-smith", "action-with-all-types-of-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-all-types-of-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar_Untyped,
            "baz-goo" to bazGoo_Untyped,
            binKin_Untyped?.let { "bin-kin" to it },
            "int-pint" to intPint_Untyped,
            "flo-pint" to floPint_Untyped,
            "fin-bin" to finBin_Untyped,
            "goo-zen" to gooZen_Untyped,
            "bah-enum" to bahEnum_Untyped,
            listStrings_Untyped?.let { "list-strings" to it },
            listInts_Untyped?.let { "list-ints" to it },
            listEnums_Untyped?.let { "list-enums" to it },
            listIntSpecial_Untyped?.let { "list-int-special" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithAllTypesOfInputsBindingV2_Untyped
        return fooBar_Untyped == other.fooBar_Untyped &&
            bazGoo_Untyped == other.bazGoo_Untyped &&
            binKin_Untyped == other.binKin_Untyped &&
            intPint_Untyped == other.intPint_Untyped &&
            floPint_Untyped == other.floPint_Untyped &&
            finBin_Untyped == other.finBin_Untyped &&
            gooZen_Untyped == other.gooZen_Untyped &&
            bahEnum_Untyped == other.bahEnum_Untyped &&
            listStrings_Untyped == other.listStrings_Untyped &&
            listInts_Untyped == other.listInts_Untyped &&
            listEnums_Untyped == other.listEnums_Untyped &&
            listIntSpecial_Untyped == other.listIntSpecial_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        fooBar_Untyped,
        bazGoo_Untyped,
        binKin_Untyped,
        intPint_Untyped,
        floPint_Untyped,
        finBin_Untyped,
        gooZen_Untyped,
        bahEnum_Untyped,
        listStrings_Untyped,
        listInts_Untyped,
        listEnums_Untyped,
        listIntSpecial_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithAllTypesOfInputsBindingV2_Untyped(")
        append("""fooBar_Untyped=$fooBar_Untyped""")
        append(", ")
        append("""bazGoo_Untyped=$bazGoo_Untyped""")
        append(", ")
        append("""binKin_Untyped=$binKin_Untyped""")
        append(", ")
        append("""intPint_Untyped=$intPint_Untyped""")
        append(", ")
        append("""floPint_Untyped=$floPint_Untyped""")
        append(", ")
        append("""finBin_Untyped=$finBin_Untyped""")
        append(", ")
        append("""gooZen_Untyped=$gooZen_Untyped""")
        append(", ")
        append("""bahEnum_Untyped=$bahEnum_Untyped""")
        append(", ")
        append("""listStrings_Untyped=$listStrings_Untyped""")
        append(", ")
        append("""listInts_Untyped=$listInts_Untyped""")
        append(", ")
        append("""listEnums_Untyped=$listEnums_Untyped""")
        append(", ")
        append("""listIntSpecial_Untyped=$listIntSpecial_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param fooBar_Untyped Short description
     * @param bazGoo_Untyped First boolean input!
     * @param binKin_Untyped Boolean and nullable
     * @param intPint_Untyped Integer
     * @param floPint_Untyped Float
     * @param finBin_Untyped Enumeration
     * @param gooZen_Untyped Integer with special value
     * @param bahEnum_Untyped Enum with custom naming
     * @param listStrings_Untyped List of strings
     * @param listInts_Untyped List of integers
     * @param listEnums_Untyped List of enums
     * @param listIntSpecial_Untyped List of integer with special values
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        fooBar_Untyped: String = this.fooBar_Untyped,
        bazGoo_Untyped: String = this.bazGoo_Untyped,
        binKin_Untyped: String? = this.binKin_Untyped,
        intPint_Untyped: String = this.intPint_Untyped,
        floPint_Untyped: String = this.floPint_Untyped,
        finBin_Untyped: String = this.finBin_Untyped,
        gooZen_Untyped: String = this.gooZen_Untyped,
        bahEnum_Untyped: String = this.bahEnum_Untyped,
        listStrings_Untyped: String? = this.listStrings_Untyped,
        listInts_Untyped: String? = this.listInts_Untyped,
        listEnums_Untyped: String? = this.listEnums_Untyped,
        listIntSpecial_Untyped: String? = this.listIntSpecial_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithAllTypesOfInputsBindingV2_Untyped = ActionWithAllTypesOfInputsBindingV2_Untyped(
        fooBar_Untyped = fooBar_Untyped,
        bazGoo_Untyped = bazGoo_Untyped,
        binKin_Untyped = binKin_Untyped,
        intPint_Untyped = intPint_Untyped,
        floPint_Untyped = floPint_Untyped,
        finBin_Untyped = finBin_Untyped,
        gooZen_Untyped = gooZen_Untyped,
        bahEnum_Untyped = bahEnum_Untyped,
        listStrings_Untyped = listStrings_Untyped,
        listInts_Untyped = listInts_Untyped,
        listEnums_Untyped = listEnums_Untyped,
        listIntSpecial_Untyped = listIntSpecial_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Cool output!
         */
        public val bazGoo: String = "steps.$stepId.outputs.baz-goo"
    }
}
