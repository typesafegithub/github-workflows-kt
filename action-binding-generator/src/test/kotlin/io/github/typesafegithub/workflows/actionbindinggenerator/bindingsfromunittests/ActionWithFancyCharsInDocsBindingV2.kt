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
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-fancy-chars-in-docs-binding-v2)
 *
 * @param nestedKotlinComments This is a /&#42; test &#42;/
 * @param nestedKotlinComments_Untyped This is a /&#42; test &#42;/
 * @param percent For example "100%"
 * @param percent_Untyped For example "100%"
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class ActionWithFancyCharsInDocsBindingV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * This is a /&#42; test &#42;/
     */
    public val nestedKotlinComments: String? = null,
    /**
     * This is a /&#42; test &#42;/
     */
    public val nestedKotlinComments_Untyped: String? = null,
    /**
     * For example "100%"
     */
    public val percent: String? = null,
    /**
     * For example "100%"
     */
    public val percent_Untyped: String? = null,
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

        require(!((nestedKotlinComments != null) && (nestedKotlinComments_Untyped != null))) {
            "Only nestedKotlinComments or nestedKotlinComments_Untyped must be set, but not both"
        }

        require(!((percent != null) && (percent_Untyped != null))) {
            "Only percent or percent_Untyped must be set, but not both"
        }
    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            nestedKotlinComments?.let { "nested-kotlin-comments" to it },
            nestedKotlinComments_Untyped?.let { "nested-kotlin-comments" to it },
            percent?.let { "percent" to it },
            percent_Untyped?.let { "percent" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithFancyCharsInDocsBindingV2
        return nestedKotlinComments == other.nestedKotlinComments &&
            nestedKotlinComments_Untyped == other.nestedKotlinComments_Untyped &&
            percent == other.percent &&
            percent_Untyped == other.percent_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        nestedKotlinComments,
        nestedKotlinComments_Untyped,
        percent,
        percent_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithFancyCharsInDocsBindingV2(")
        append("""nestedKotlinComments=$nestedKotlinComments""")
        append(", ")
        append("""nestedKotlinComments_Untyped=$nestedKotlinComments_Untyped""")
        append(", ")
        append("""percent=$percent""")
        append(", ")
        append("""percent_Untyped=$percent_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param nestedKotlinComments This is a /&#42; test &#42;/
     * @param nestedKotlinComments_Untyped This is a /&#42; test &#42;/
     * @param percent For example "100%"
     * @param percent_Untyped For example "100%"
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        nestedKotlinComments: String? = this.nestedKotlinComments,
        nestedKotlinComments_Untyped: String? = this.nestedKotlinComments_Untyped,
        percent: String? = this.percent,
        percent_Untyped: String? = this.percent_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithFancyCharsInDocsBindingV2 = ActionWithFancyCharsInDocsBindingV2(
        nestedKotlinComments = nestedKotlinComments,
        nestedKotlinComments_Untyped = nestedKotlinComments_Untyped,
        percent = percent,
        percent_Untyped = percent_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
