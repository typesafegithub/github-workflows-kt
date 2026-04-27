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
 * Action: Do something cool
 * and describe it in multiple lines
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/simple-action-with-required-string-inputs-binding-v2)
 *
 * @param fooBar &lt;required&gt; Short description
 * @param fooBar_Untyped &lt;required&gt; Short description
 * @param bazGoo &lt;required&gt; Just another input
 * with multiline description
 * @param bazGoo_Untyped &lt;required&gt; Just another input
 * with multiline description
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class SimpleActionWithRequiredStringInputsBindingV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * &lt;required&gt; Short description
     */
    public val fooBar: String? = null,
    /**
     * &lt;required&gt; Short description
     */
    public val fooBar_Untyped: String? = null,
    /**
     * &lt;required&gt; Just another input
     * with multiline description
     */
    @Deprecated("this is deprecated")
    public val bazGoo: String? = null,
    /**
     * &lt;required&gt; Just another input
     * with multiline description
     */
    @Deprecated("this is deprecated")
    public val bazGoo_Untyped: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "simple-action-with-required-string-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/simple-action-with-required-string-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/simple-action-with-required-string-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(!((fooBar != null) && (fooBar_Untyped != null))) {
            "Only fooBar or fooBar_Untyped must be set, but not both"
        }
        require((fooBar != null) || (fooBar_Untyped != null)) {
            "Either fooBar or fooBar_Untyped must be set, one of them is required"
        }

        require(!((bazGoo != null) && (bazGoo_Untyped != null))) {
            "Only bazGoo or bazGoo_Untyped must be set, but not both"
        }
        require((bazGoo != null) || (bazGoo_Untyped != null)) {
            "Either bazGoo or bazGoo_Untyped must be set, one of them is required"
        }
    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            fooBar_Untyped?.let { "foo-bar" to it },
            bazGoo?.let { "baz-goo" to it },
            bazGoo_Untyped?.let { "baz-goo" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SimpleActionWithRequiredStringInputsBindingV2
        return fooBar == other.fooBar &&
            fooBar_Untyped == other.fooBar_Untyped &&
            bazGoo == other.bazGoo &&
            bazGoo_Untyped == other.bazGoo_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        fooBar,
        fooBar_Untyped,
        bazGoo,
        bazGoo_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("SimpleActionWithRequiredStringInputsBindingV2(")
        append("""fooBar=$fooBar""")
        append(", ")
        append("""fooBar_Untyped=$fooBar_Untyped""")
        append(", ")
        append("""bazGoo=$bazGoo""")
        append(", ")
        append("""bazGoo_Untyped=$bazGoo_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param fooBar &lt;required&gt; Short description
     * @param fooBar_Untyped &lt;required&gt; Short description
     * @param bazGoo &lt;required&gt; Just another input
     * with multiline description
     * @param bazGoo_Untyped &lt;required&gt; Just another input
     * with multiline description
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = this.fooBar,
        fooBar_Untyped: String? = this.fooBar_Untyped,
        bazGoo: String? = this.bazGoo,
        bazGoo_Untyped: String? = this.bazGoo_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): SimpleActionWithRequiredStringInputsBindingV2 = SimpleActionWithRequiredStringInputsBindingV2(
        fooBar = fooBar,
        fooBar_Untyped = fooBar_Untyped,
        bazGoo = bazGoo,
        bazGoo_Untyped = bazGoo_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
