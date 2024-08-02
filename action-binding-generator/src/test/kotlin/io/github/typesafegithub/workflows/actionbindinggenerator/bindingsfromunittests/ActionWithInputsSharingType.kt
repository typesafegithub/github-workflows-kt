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
 * [Action on GitHub](https://github.com/john-smith/action-with-inputs-sharing-type)
 *
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class ActionWithInputsSharingType private constructor(
    public val fooOne: ActionWithInputsSharingType.Foo,
    public val fooTwo: ActionWithInputsSharingType.Foo,
    public val fooThree: ActionWithInputsSharingType.Foo? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-inputs-sharing-type", _customVersion ?:
        "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooOne: ActionWithInputsSharingType.Foo,
        fooTwo: ActionWithInputsSharingType.Foo,
        fooThree: ActionWithInputsSharingType.Foo? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooOne=fooOne, fooTwo=fooTwo, fooThree=fooThree, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-one" to fooOne.integerValue.toString(),
            "foo-two" to fooTwo.integerValue.toString(),
            fooThree?.let { "foo-three" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Foo(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithInputsSharingType.Foo(requestedValue)

        public object Special1 : ActionWithInputsSharingType.Foo(3)
    }
}
