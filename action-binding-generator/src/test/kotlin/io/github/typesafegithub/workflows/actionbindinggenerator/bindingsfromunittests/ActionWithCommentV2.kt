// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress("UNUSED_PARAMETER")

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import java.util.Objects
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
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
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class ActionWithCommentV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * &lt;required&gt; Short description
     */
    public val foo: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    public val foo_Untyped: String? = null,
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

        require(!((foo != null) && (foo_Untyped != null))) {
            "Only foo or foo_Untyped must be set, but not both"
        }
        require((foo != null) || (foo_Untyped != null)) {
            "Either foo or foo_Untyped must be set, one of them is required"
        }
    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            foo?.let { "foo" to it },
            foo_Untyped?.let { "foo" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithCommentV2
        return foo == other.foo &&
            foo_Untyped == other.foo_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        foo,
        foo_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithCommentV2(")
        append("""foo=$foo""")
        append(", ")
        append("""foo_Untyped=$foo_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param foo &lt;required&gt; Short description
     * @param foo_Untyped &lt;required&gt; Short description
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        foo: String? = this.foo,
        foo_Untyped: String? = this.foo_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithCommentV2 = ActionWithCommentV2(
        foo = foo,
        foo_Untyped = foo_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
