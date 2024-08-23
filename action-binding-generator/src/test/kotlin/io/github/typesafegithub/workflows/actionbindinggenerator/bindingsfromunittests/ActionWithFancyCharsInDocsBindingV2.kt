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
 * [Action on GitHub](https://github.com/john-smith/action-with-fancy-chars-in-docs-binding-v2)
 *
 * @param nestedKotlinComments This is a /&#42; test &#42;/
 * @param nestedKotlinComments_Untyped This is a /&#42; test &#42;/
 * @param nestedKotlinCommentsExpression This is a /&#42; test &#42;/
 * @param percent For example "100%"
 * @param percent_Untyped For example "100%"
 * @param percentExpression For example "100%"
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithFancyCharsInDocsBindingV2 private constructor(
    /**
     * This is a /&#42; test &#42;/
     */
    public val nestedKotlinComments: String? = null,
    /**
     * This is a /&#42; test &#42;/
     */
    @Deprecated("Use the typed property or expression property instead")
    public val nestedKotlinComments_Untyped: String? = null,
    /**
     * This is a /&#42; test &#42;/
     */
    public val nestedKotlinCommentsExpression: Expression<String>? = null,
    /**
     * For example "100%"
     */
    public val percent: String? = null,
    /**
     * For example "100%"
     */
    @Deprecated("Use the typed property or expression property instead")
    public val percent_Untyped: String? = null,
    /**
     * For example "100%"
     */
    public val percentExpression: Expression<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-fancy-chars-in-docs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-fancy-chars-in-docs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-fancy-chars-in-docs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(nestedKotlinComments, nestedKotlinComments_Untyped, nestedKotlinCommentsExpression).size <= 1) {
            "Only one of nestedKotlinComments, nestedKotlinComments_Untyped, and nestedKotlinCommentsExpression must be set, but not multiple"
        }

        require(listOfNotNull(percent, percent_Untyped, percentExpression).size <= 1) {
            "Only one of percent, percent_Untyped, and percentExpression must be set, but not multiple"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        nestedKotlinComments: String? = null,
        nestedKotlinComments_Untyped: String? = null,
        nestedKotlinCommentsExpression: Expression<String>? = null,
        percent: String? = null,
        percent_Untyped: String? = null,
        percentExpression: Expression<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(nestedKotlinComments = nestedKotlinComments, nestedKotlinComments_Untyped = nestedKotlinComments_Untyped, nestedKotlinCommentsExpression = nestedKotlinCommentsExpression, percent = percent, percent_Untyped = percent_Untyped, percentExpression = percentExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            nestedKotlinComments?.let { "nested-kotlin-comments" to it },
            nestedKotlinComments_Untyped?.let { "nested-kotlin-comments" to it },
            nestedKotlinCommentsExpression?.let { "nested-kotlin-comments" to it.expressionString },
            percent?.let { "percent" to it },
            percent_Untyped?.let { "percent" to it },
            percentExpression?.let { "percent" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
