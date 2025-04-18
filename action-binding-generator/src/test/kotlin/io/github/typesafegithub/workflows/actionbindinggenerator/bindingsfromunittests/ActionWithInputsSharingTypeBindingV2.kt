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
 * [Action on GitHub](https://github.com/john-smith/action-with-inputs-sharing-type-binding-v2)
 *
 * @param fooOne &lt;required&gt;
 * @param fooOne_Untyped &lt;required&gt;
 * @param fooTwo &lt;required&gt;
 * @param fooTwo_Untyped &lt;required&gt;
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class ActionWithInputsSharingTypeBindingV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * &lt;required&gt;
     */
    public val fooOne: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    /**
     * &lt;required&gt;
     */
    public val fooOne_Untyped: String? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwo: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwo_Untyped: String? = null,
    public val fooThree: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    public val fooThree_Untyped: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-inputs-sharing-type-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-inputs-sharing-type-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-inputs-sharing-type-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

        require(!((fooOne != null) && (fooOne_Untyped != null))) {
            "Only fooOne or fooOne_Untyped must be set, but not both"
        }
        require((fooOne != null) || (fooOne_Untyped != null)) {
            "Either fooOne or fooOne_Untyped must be set, one of them is required"
        }

        require(!((fooTwo != null) && (fooTwo_Untyped != null))) {
            "Only fooTwo or fooTwo_Untyped must be set, but not both"
        }
        require((fooTwo != null) || (fooTwo_Untyped != null)) {
            "Either fooTwo or fooTwo_Untyped must be set, one of them is required"
        }

        require(!((fooThree != null) && (fooThree_Untyped != null))) {
            "Only fooThree or fooThree_Untyped must be set, but not both"
        }
    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooOne?.let { "foo-one" to it.integerValue.toString() },
            fooOne_Untyped?.let { "foo-one" to it },
            fooTwo?.let { "foo-two" to it.integerValue.toString() },
            fooTwo_Untyped?.let { "foo-two" to it },
            fooThree?.let { "foo-three" to it.integerValue.toString() },
            fooThree_Untyped?.let { "foo-three" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithInputsSharingTypeBindingV2
        return fooOne == other.fooOne &&
            fooOne_Untyped == other.fooOne_Untyped &&
            fooTwo == other.fooTwo &&
            fooTwo_Untyped == other.fooTwo_Untyped &&
            fooThree == other.fooThree &&
            fooThree_Untyped == other.fooThree_Untyped &&
            _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        fooOne,
        fooOne_Untyped,
        fooTwo,
        fooTwo_Untyped,
        fooThree,
        fooThree_Untyped,
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithInputsSharingTypeBindingV2(")
        append("""fooOne=$fooOne""")
        append(", ")
        append("""fooOne_Untyped=$fooOne_Untyped""")
        append(", ")
        append("""fooTwo=$fooTwo""")
        append(", ")
        append("""fooTwo_Untyped=$fooTwo_Untyped""")
        append(", ")
        append("""fooThree=$fooThree""")
        append(", ")
        append("""fooThree_Untyped=$fooThree_Untyped""")
        append(", ")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param fooOne &lt;required&gt;
     * @param fooOne_Untyped &lt;required&gt;
     * @param fooTwo &lt;required&gt;
     * @param fooTwo_Untyped &lt;required&gt;
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        fooOne: ActionWithInputsSharingTypeBindingV2.Foo? = this.fooOne,
        fooOne_Untyped: String? = this.fooOne_Untyped,
        fooTwo: ActionWithInputsSharingTypeBindingV2.Foo? = this.fooTwo,
        fooTwo_Untyped: String? = this.fooTwo_Untyped,
        fooThree: ActionWithInputsSharingTypeBindingV2.Foo? = this.fooThree,
        fooThree_Untyped: String? = this.fooThree_Untyped,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithInputsSharingTypeBindingV2 = ActionWithInputsSharingTypeBindingV2(
        fooOne = fooOne,
        fooOne_Untyped = fooOne_Untyped,
        fooTwo = fooTwo,
        fooTwo_Untyped = fooTwo_Untyped,
        fooThree = fooThree,
        fooThree_Untyped = fooThree_Untyped,
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Foo(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithInputsSharingTypeBindingV2.Foo(requestedValue)

        public object Special1 : ActionWithInputsSharingTypeBindingV2.Foo(3)
    }
}
