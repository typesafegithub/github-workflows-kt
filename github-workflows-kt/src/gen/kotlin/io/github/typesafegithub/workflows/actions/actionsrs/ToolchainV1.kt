// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
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
 * Action: rust-toolchain
 *
 * Install the Rust toolchain
 *
 * [Action on GitHub](https://github.com/actions-rs/toolchain)
 *
 * @param toolchain Rust toolchain name.
 *
 * See https://rust-lang.github.io/rustup/concepts/toolchains.html#toolchain-specification
 *
 * If this is not given, the action will try and install the version specified in the
 * `rust-toolchain` file.
 * @param target Target triple to install for this toolchain
 * @param default Set installed toolchain as default
 * @param override Set installed toolchain as an override for a directory
 * @param profile Name of the group of components to be installed for a new toolchain
 * @param components Comma-separated list of components to be additionally installed for a new
 * toolchain
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class ToolchainV1 private constructor(
    /**
     * Rust toolchain name.
     *
     * See https://rust-lang.github.io/rustup/concepts/toolchains.html#toolchain-specification
     *
     * If this is not given, the action will try and install the version specified in the
     * `rust-toolchain` file.
     */
    public val toolchain: String? = null,
    /**
     * Target triple to install for this toolchain
     */
    public val target: String? = null,
    /**
     * Set installed toolchain as default
     */
    public val default: Boolean? = null,
    /**
     * Set installed toolchain as an override for a directory
     */
    public val `override`: Boolean? = null,
    /**
     * Name of the group of components to be installed for a new toolchain
     */
    public val profile: String? = null,
    /**
     * Comma-separated list of components to be additionally installed for a new toolchain
     */
    public val components: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<ToolchainV1.Outputs>("actions-rs", "toolchain", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        toolchain: String? = null,
        target: String? = null,
        default: Boolean? = null,
        `override`: Boolean? = null,
        profile: String? = null,
        components: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(toolchain=toolchain, target=target, default=default, `override`=`override`,
            profile=profile, components=components, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            toolchain?.let { "toolchain" to it },
            target?.let { "target" to it },
            default?.let { "default" to it.toString() },
            `override`?.let { "override" to it.toString() },
            profile?.let { "profile" to it },
            components?.let { "components" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Installed Rustc version
         */
        public val rustc: String = "steps.$stepId.outputs.rustc"

        /**
         * Installed Rustc version hash, can be used for caching purposes
         */
        public val rustcHash: String = "steps.$stepId.outputs.rustc_hash"

        /**
         * Installed Cargo version
         */
        public val cargo: String = "steps.$stepId.outputs.cargo"

        /**
         * Installed rustup version
         */
        public val rustup: String = "steps.$stepId.outputs.rustup"
    }
}
