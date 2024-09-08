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
 * Action: Some Action
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-deprecated-input-and-name-clash-binding-v2)
 *
 * @param fooBar &lt;required&gt; Foo bar - new
 * @param fooBar_Untyped &lt;required&gt; Foo bar - new
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class ActionWithDeprecatedInputAndNameClashBindingV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * &lt;required&gt; Foo bar - new
     */
    public val fooBar: String? = null,
    /**
     * &lt;required&gt; Foo bar - new
     */
    public val fooBar_Untyped: String? = null,
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

        require(!((fooBar != null) && (fooBar_Untyped != null))) {
            "Only fooBar or fooBar_Untyped must be set, but not both"
        }
        require((fooBar != null) || (fooBar_Untyped != null)) {
            "Either fooBar or fooBar_Untyped must be set, one of them is required"
        }
    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "fooBar" to it },
            fooBar_Untyped?.let { "fooBar" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithDeprecatedInputAndNameClashBindingV2
        return fooBar == other.fooBar &&
            fooBar_Untyped == other.fooBar_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        fooBar,
        fooBar_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithDeprecatedInputAndNameClashBindingV2(")
        append("""fooBar=$fooBar""")
        append(", ")
        append("""fooBar_Untyped=$fooBar_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param fooBar &lt;required&gt; Foo bar - new
     * @param fooBar_Untyped &lt;required&gt; Foo bar - new
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = this.fooBar,
        fooBar_Untyped: String? = this.fooBar_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithDeprecatedInputAndNameClashBindingV2 = ActionWithDeprecatedInputAndNameClashBindingV2(
        fooBar = fooBar,
        fooBar_Untyped = fooBar_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
