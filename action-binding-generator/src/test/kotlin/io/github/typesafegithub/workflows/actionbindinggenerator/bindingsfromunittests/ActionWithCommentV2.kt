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
 * Action: Action with comment
 *
 * Do something cool
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-comment-v2)
 *
 * @param foo &lt;required&gt; Short description
 * @param foo_Untyped &lt;required&gt; Short description
 * @param fooExpression &lt;required&gt; Short description
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithCommentV2 private constructor(
    /**
     * &lt;required&gt; Short description
     */
    public val foo: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    @Deprecated("Use the typed property or expression property instead")
    public val foo_Untyped: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    public val fooExpression: Expression<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-comment-v2", _customVersion ?: "v3", "some-comment") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-comment-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-comment-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(listOfNotNull(foo, foo_Untyped, fooExpression).size <= 1) {
            "Only one of foo, foo_Untyped, and fooExpression must be set, but not multiple"
        }
        require((foo != null) || (foo_Untyped != null) || (fooExpression != null)) {
            "Either foo, foo_Untyped, or fooExpression must be set, one of them is required"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        foo: String? = null,
        foo_Untyped: String? = null,
        fooExpression: Expression<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(foo = foo, foo_Untyped = foo_Untyped, fooExpression = fooExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            foo?.let { "foo" to it },
            foo_Untyped?.let { "foo" to it },
            fooExpression?.let { "foo" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
