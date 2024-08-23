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
import kotlin.Deprecated
import kotlin.ExposedCopyVisibility
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-some-optional-inputs-binding-v2)
 *
 * @param fooBar Required is default, default is set
 * @param fooBar_Untyped Required is default, default is set
 * @param fooBarExpression Required is default, default is set
 * @param bazGoo Required is default, default is null
 * @param bazGoo_Untyped Required is default, default is null
 * @param bazGooExpression Required is default, default is null
 * @param zooDar Required is false, default is set
 * @param zooDar_Untyped Required is false, default is set
 * @param zooDarExpression Required is false, default is set
 * @param cooPoo Required is false, default is default
 * @param cooPoo_Untyped Required is false, default is default
 * @param cooPooExpression Required is false, default is default
 * @param package &lt;required&gt; Required is true, default is default
 * @param package_Untyped &lt;required&gt; Required is true, default is default
 * @param packageExpression &lt;required&gt; Required is true, default is default
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithSomeOptionalInputsBindingV2 private constructor(
    /**
     * Required is default, default is set
     */
    public val fooBar: String? = null,
    /**
     * Required is default, default is set
     */
    @Deprecated("Use the typed property or expression property instead")
    public val fooBar_Untyped: String? = null,
    /**
     * Required is default, default is set
     */
    public val fooBarExpression: Expression<String>? = null,
    /**
     * Required is default, default is null
     */
    public val bazGoo: String? = null,
    /**
     * Required is default, default is null
     */
    @Deprecated("Use the typed property or expression property instead")
    public val bazGoo_Untyped: String? = null,
    /**
     * Required is default, default is null
     */
    public val bazGooExpression: Expression<String>? = null,
    /**
     * Required is false, default is set
     */
    public val zooDar: String? = null,
    /**
     * Required is false, default is set
     */
    @Deprecated("Use the typed property or expression property instead")
    public val zooDar_Untyped: String? = null,
    /**
     * Required is false, default is set
     */
    public val zooDarExpression: Expression<String>? = null,
    /**
     * Required is false, default is default
     */
    public val cooPoo: String? = null,
    /**
     * Required is false, default is default
     */
    @Deprecated("Use the typed property or expression property instead")
    public val cooPoo_Untyped: String? = null,
    /**
     * Required is false, default is default
     */
    public val cooPooExpression: Expression<String>? = null,
    /**
     * &lt;required&gt; Required is true, default is default
     */
    public val `package`: String? = null,
    /**
     * &lt;required&gt; Required is true, default is default
     */
    @Deprecated("Use the typed property or expression property instead")
    public val package_Untyped: String? = null,
    /**
     * &lt;required&gt; Required is true, default is default
     */
    public val packageExpression: Expression<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-some-optional-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-some-optional-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-some-optional-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(fooBar, fooBar_Untyped, fooBarExpression).size <= 1) {
            "Only one of fooBar, fooBar_Untyped, and fooBarExpression must be set, but not multiple"
        }

        require(listOfNotNull(bazGoo, bazGoo_Untyped, bazGooExpression).size <= 1) {
            "Only one of bazGoo, bazGoo_Untyped, and bazGooExpression must be set, but not multiple"
        }

        require(listOfNotNull(zooDar, zooDar_Untyped, zooDarExpression).size <= 1) {
            "Only one of zooDar, zooDar_Untyped, and zooDarExpression must be set, but not multiple"
        }

        require(listOfNotNull(cooPoo, cooPoo_Untyped, cooPooExpression).size <= 1) {
            "Only one of cooPoo, cooPoo_Untyped, and cooPooExpression must be set, but not multiple"
        }

        require(listOfNotNull(`package`, package_Untyped, packageExpression).size <= 1) {
            "Only one of package, package_Untyped, and packageExpression must be set, but not multiple"
        }
        require((`package` != null) || (package_Untyped != null) || (packageExpression != null)) {
            "Either package, package_Untyped, or packageExpression must be set, one of them is required"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = null,
        fooBar_Untyped: String? = null,
        fooBarExpression: Expression<String>? = null,
        bazGoo: String? = null,
        bazGoo_Untyped: String? = null,
        bazGooExpression: Expression<String>? = null,
        zooDar: String? = null,
        zooDar_Untyped: String? = null,
        zooDarExpression: Expression<String>? = null,
        cooPoo: String? = null,
        cooPoo_Untyped: String? = null,
        cooPooExpression: Expression<String>? = null,
        `package`: String? = null,
        package_Untyped: String? = null,
        packageExpression: Expression<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar = fooBar, fooBar_Untyped = fooBar_Untyped, fooBarExpression = fooBarExpression, bazGoo = bazGoo, bazGoo_Untyped = bazGoo_Untyped, bazGooExpression = bazGooExpression, zooDar = zooDar, zooDar_Untyped = zooDar_Untyped, zooDarExpression = zooDarExpression, cooPoo = cooPoo, cooPoo_Untyped = cooPoo_Untyped, cooPooExpression = cooPooExpression, `package` = `package`, package_Untyped = package_Untyped, packageExpression = packageExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            fooBar_Untyped?.let { "foo-bar" to it },
            fooBarExpression?.let { "foo-bar" to it.expressionString },
            bazGoo?.let { "baz-goo" to it },
            bazGoo_Untyped?.let { "baz-goo" to it },
            bazGooExpression?.let { "baz-goo" to it.expressionString },
            zooDar?.let { "zoo-dar" to it },
            zooDar_Untyped?.let { "zoo-dar" to it },
            zooDarExpression?.let { "zoo-dar" to it.expressionString },
            cooPoo?.let { "coo-poo" to it },
            cooPoo_Untyped?.let { "coo-poo" to it },
            cooPooExpression?.let { "coo-poo" to it.expressionString },
            `package`?.let { "package" to it },
            package_Untyped?.let { "package" to it },
            packageExpression?.let { "package" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
