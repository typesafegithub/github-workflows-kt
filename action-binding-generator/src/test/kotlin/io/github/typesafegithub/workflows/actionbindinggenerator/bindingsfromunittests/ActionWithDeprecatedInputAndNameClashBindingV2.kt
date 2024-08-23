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
 * Action: Some Action
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-deprecated-input-and-name-clash-binding-v2)
 *
 * @param fooBar &lt;required&gt; Foo bar - new
 * @param fooBar_Untyped &lt;required&gt; Foo bar - new
 * @param fooBarExpression &lt;required&gt; Foo bar - new
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithDeprecatedInputAndNameClashBindingV2 private constructor(
    /**
     * &lt;required&gt; Foo bar - new
     */
    public val fooBar: String? = null,
    /**
     * &lt;required&gt; Foo bar - new
     */
    @Deprecated("Use the typed property or expression property instead")
    public val fooBar_Untyped: String? = null,
    /**
     * &lt;required&gt; Foo bar - new
     */
    public val fooBarExpression: Expression<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-deprecated-input-and-name-clash-binding-v2", _customVersion ?: "v2") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-deprecated-input-and-name-clash-binding-v2@v2 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-deprecated-input-and-name-clash-binding-v2@v2 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(fooBar, fooBar_Untyped, fooBarExpression).size <= 1) {
            "Only one of fooBar, fooBar_Untyped, and fooBarExpression must be set, but not multiple"
        }
        require((fooBar != null) || (fooBar_Untyped != null) || (fooBarExpression != null)) {
            "Either fooBar, fooBar_Untyped, or fooBarExpression must be set, one of them is required"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = null,
        fooBar_Untyped: String? = null,
        fooBarExpression: Expression<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar = fooBar, fooBar_Untyped = fooBar_Untyped, fooBarExpression = fooBarExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "fooBar" to it },
            fooBar_Untyped?.let { "fooBar" to it },
            fooBarExpression?.let { "fooBar" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
