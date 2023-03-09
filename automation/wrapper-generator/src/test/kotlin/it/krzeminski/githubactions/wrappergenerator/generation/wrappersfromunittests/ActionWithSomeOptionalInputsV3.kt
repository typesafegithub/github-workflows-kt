// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
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
 * [Action on GitHub](https://github.com/john-smith/action-with-some-optional-inputs)
 */
public data class ActionWithSomeOptionalInputsV3 private constructor(
    /**
     * Required is default, default is set
     */
    public val fooBar: String? = null,
    /**
     * Required is default, default is null
     */
    public val bazGoo: String? = null,
    /**
     * Required is false, default is set
     */
    public val zooDar: String? = null,
    /**
     * Required is false, default is default
     */
    public val cooPoo: String? = null,
    /**
     * Required is true, default is default
     */
    public val `package`: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("john-smith", "action-with-some-optional-inputs", _customVersion ?: "v3")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String? = null,
        bazGoo: String? = null,
        zooDar: String? = null,
        cooPoo: String? = null,
        `package`: String,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar=fooBar, bazGoo=bazGoo, zooDar=zooDar, cooPoo=cooPoo, `package`=`package`,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            bazGoo?.let { "baz-goo" to it },
            zooDar?.let { "zoo-dar" to it },
            cooPoo?.let { "coo-poo" to it },
            "package" to `package`,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
