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
 * @param fooOneExpression &lt;required&gt;
 * @param fooTwo &lt;required&gt;
 * @param fooTwo_Untyped &lt;required&gt;
 * @param fooTwoExpression &lt;required&gt;
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithInputsSharingTypeBindingV2 private constructor(
    /**
     * &lt;required&gt;
     */
    public val fooOne: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    /**
     * &lt;required&gt;
     */
    @Deprecated("Use the typed property or expression property instead")
    public val fooOne_Untyped: String? = null,
    /**
     * &lt;required&gt;
     */
    public val fooOneExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwo: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    /**
     * &lt;required&gt;
     */
    @Deprecated("Use the typed property or expression property instead")
    public val fooTwo_Untyped: String? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwoExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
    public val fooThree: ActionWithInputsSharingTypeBindingV2.Foo? = null,
    @Deprecated("Use the typed property or expression property instead")
    public val fooThree_Untyped: String? = null,
    public val fooThreeExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
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

        require(listOfNotNull(fooOne, fooOne_Untyped, fooOneExpression).size <= 1) {
            "Only one of fooOne, fooOne_Untyped, and fooOneExpression must be set, but not multiple"
        }
        require((fooOne != null) || (fooOne_Untyped != null) || (fooOneExpression != null)) {
            "Either fooOne, fooOne_Untyped, or fooOneExpression must be set, one of them is required"
        }

        require(listOfNotNull(fooTwo, fooTwo_Untyped, fooTwoExpression).size <= 1) {
            "Only one of fooTwo, fooTwo_Untyped, and fooTwoExpression must be set, but not multiple"
        }
        require((fooTwo != null) || (fooTwo_Untyped != null) || (fooTwoExpression != null)) {
            "Either fooTwo, fooTwo_Untyped, or fooTwoExpression must be set, one of them is required"
        }

        require(listOfNotNull(fooThree, fooThree_Untyped, fooThreeExpression).size <= 1) {
            "Only one of fooThree, fooThree_Untyped, and fooThreeExpression must be set, but not multiple"
        }
    }

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooOne: ActionWithInputsSharingTypeBindingV2.Foo? = null,
        fooOne_Untyped: String? = null,
        fooOneExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
        fooTwo: ActionWithInputsSharingTypeBindingV2.Foo? = null,
        fooTwo_Untyped: String? = null,
        fooTwoExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
        fooThree: ActionWithInputsSharingTypeBindingV2.Foo? = null,
        fooThree_Untyped: String? = null,
        fooThreeExpression: Expression<ActionWithInputsSharingTypeBindingV2.Foo>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooOne = fooOne, fooOne_Untyped = fooOne_Untyped, fooOneExpression = fooOneExpression, fooTwo = fooTwo, fooTwo_Untyped = fooTwo_Untyped, fooTwoExpression = fooTwoExpression, fooThree = fooThree, fooThree_Untyped = fooThree_Untyped, fooThreeExpression = fooThreeExpression, _customInputs = _customInputs, _customVersion = _customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooOne?.let { "foo-one" to it.integerValue.toString() },
            fooOne_Untyped?.let { "foo-one" to it },
            fooOneExpression?.let { "foo-one" to it.expressionString },
            fooTwo?.let { "foo-two" to it.integerValue.toString() },
            fooTwo_Untyped?.let { "foo-two" to it },
            fooTwoExpression?.let { "foo-two" to it.expressionString },
            fooThree?.let { "foo-three" to it.integerValue.toString() },
            fooThree_Untyped?.let { "foo-three" to it },
            fooThreeExpression?.let { "foo-three" to it.expressionString },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
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
