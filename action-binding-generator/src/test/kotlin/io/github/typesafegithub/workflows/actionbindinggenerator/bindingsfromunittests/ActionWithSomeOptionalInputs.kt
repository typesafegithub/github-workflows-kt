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
 *
 * @param fooBar Required is default, default is set
 * @param bazGoo Required is default, default is null
 * @param zooDar Required is false, default is set
 * @param cooPoo Required is false, default is default
 * @param package Required is true, default is default
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class ActionWithSomeOptionalInputs private constructor(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-some-optional-inputs", _customVersion
        ?: "v3") {
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
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            fooBar?.let { "foo-bar" to it },
            bazGoo?.let { "baz-goo" to it },
            zooDar?.let { "zoo-dar" to it },
            cooPoo?.let { "coo-poo" to it },
            "package" to `package`,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
