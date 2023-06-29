// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actionsrs

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: rust-clippy-check
 *
 * Run clippy and annotate the diff with errors and warnings
 *
 * [Action on GitHub](https://github.com/actions-rs/clippy-check)
 */
public data class ClippyCheckV1 private constructor(
    /**
     * GitHub token
     */
    public val token: String,
    /**
     * Toolchain to use (without the `+` sign, ex. `nightly`)
     */
    public val toolchain: String? = null,
    /**
     * Arguments for the cargo command
     */
    public val args: List<String>? = null,
    /**
     * Use cross instead of cargo
     */
    public val useCross: Boolean? = null,
    /**
     * Display name of the created GitHub check. Must be unique across several
     * actions-rs/clippy-check invocations.
     */
    public val name: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions-rs", "clippy-check", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        token: String,
        toolchain: String? = null,
        args: List<String>? = null,
        useCross: Boolean? = null,
        name: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(token=token, toolchain=toolchain, args=args, useCross=useCross, name=name,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "token" to token,
            toolchain?.let { "toolchain" to it },
            args?.let { "args" to it.joinToString(" ") },
            useCross?.let { "use-cross" to it.toString() },
            name?.let { "name" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
