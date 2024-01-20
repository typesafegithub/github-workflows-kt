// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Deprecated
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
 * [Action on GitHub](https://github.com/john-smith/simple-action-with-required-string-inputs)
 *
 * @param fooBar Short description
 * @param bazGoo Just another input
 * with multiline description
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class SimpleActionWithRequiredStringInputsV3 private constructor(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * Just another input
     * with multiline description
     */
    @Deprecated("this is deprecated")
    public val bazGoo: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "simple-action-with-required-string-inputs",
        _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String,
        bazGoo: String,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar=fooBar, bazGoo=bazGoo, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            "baz-goo" to bazGoo,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
