// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-inputs-sharing-type)
 */
public data class ActionWithInputsSharingTypeV3(
    public val fooOne: ActionWithInputsSharingTypeV3.Foo,
    public val fooTwo: ActionWithInputsSharingTypeV3.Foo,
    public val fooThree: ActionWithInputsSharingTypeV3.Foo? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("john-smith", "action-with-inputs-sharing-type", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-one" to fooOne.integerValue.toString(),
            "foo-two" to fooTwo.integerValue.toString(),
            fooThree?.let { "foo-three" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class Foo(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithInputsSharingTypeV3.Foo(requestedValue)

        public object Special1 : ActionWithInputsSharingTypeV3.Foo(3)
    }
}
