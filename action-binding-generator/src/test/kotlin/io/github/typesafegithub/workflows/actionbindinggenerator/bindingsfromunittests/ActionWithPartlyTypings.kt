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
import kotlin.ExposedCopyVisibility
import kotlin.Int
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
 * [Action on GitHub](https://github.com/john-smith/action-with-partly-typings)
 *
 * @param foo &lt;required&gt;
 * @param foo_Untyped &lt;required&gt;
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithPartlyTypings private constructor(
    /**
     * &lt;required&gt;
     */
    public val foo: Int? = null,
    /**
     * &lt;required&gt;
     */
    public val foo_Untyped: String? = null,
    public val bar_Untyped: String? = null,
    public val baz_Untyped: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-partly-typings", _customVersion ?: "v3") {
    init {
        require(!((foo != null) && (foo_Untyped != null))) {
            "Only foo or foo_Untyped must be set, but not both"
        }
        require((foo != null) || (foo_Untyped != null)) {
            "Either foo or foo_Untyped must be set, one of them is required"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        foo: Int? = null,
        foo_Untyped: String? = null,
        bar_Untyped: String? = null,
        baz_Untyped: String,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(foo = foo, foo_Untyped = foo_Untyped, bar_Untyped = bar_Untyped, baz_Untyped = baz_Untyped, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            foo?.let { "foo" to it.toString() },
            foo_Untyped?.let { "foo" to it },
            bar_Untyped?.let { "bar" to it },
            "baz" to baz_Untyped,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
