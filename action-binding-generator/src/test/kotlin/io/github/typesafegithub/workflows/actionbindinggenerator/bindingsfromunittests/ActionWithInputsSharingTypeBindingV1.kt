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
 * [Action on GitHub](https://github.com/john-smith/action-with-inputs-sharing-type-binding-v1)
 *
 * @param fooOne &lt;required&gt;
 * @param fooOne_Untyped &lt;required&gt;
 * @param fooTwo &lt;required&gt;
 * @param fooTwo_Untyped &lt;required&gt;
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
@ExposedCopyVisibility
public data class ActionWithInputsSharingTypeBindingV1 private constructor(
    /**
     * &lt;required&gt;
     */
    public val fooOne: ActionWithInputsSharingTypeBindingV1.Foo? = null,
    /**
     * &lt;required&gt;
     */
    public val fooOne_Untyped: String? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwo: ActionWithInputsSharingTypeBindingV1.Foo? = null,
    /**
     * &lt;required&gt;
     */
    public val fooTwo_Untyped: String? = null,
    public val fooThree: ActionWithInputsSharingTypeBindingV1.Foo? = null,
    public val fooThree_Untyped: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-inputs-sharing-type-binding-v1", _customVersion ?: "v3") {
    init {
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

    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooOne: ActionWithInputsSharingTypeBindingV1.Foo? = null,
        fooOne_Untyped: String? = null,
        fooTwo: ActionWithInputsSharingTypeBindingV1.Foo? = null,
        fooTwo_Untyped: String? = null,
        fooThree: ActionWithInputsSharingTypeBindingV1.Foo? = null,
        fooThree_Untyped: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooOne = fooOne, fooOne_Untyped = fooOne_Untyped, fooTwo = fooTwo, fooTwo_Untyped = fooTwo_Untyped, fooThree = fooThree, fooThree_Untyped = fooThree_Untyped, _customInputs = _customInputs, _customVersion = _customVersion)

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

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Foo(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithInputsSharingTypeBindingV1.Foo(requestedValue)

        public object Special1 : ActionWithInputsSharingTypeBindingV1.Foo(3)
    }
}
