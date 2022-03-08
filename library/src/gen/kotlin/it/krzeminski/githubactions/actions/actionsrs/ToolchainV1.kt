// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actionsrs

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: rust-toolchain
 *
 * Install the Rust toolchain
 *
 * [Action on GitHub](https://github.com/actions-rs/toolchain)
 */
public class ToolchainV1(
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
    public val components: List<String>? = null
) : ActionWithOutputs<ToolchainV1.Outputs>("actions-rs", "toolchain", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            toolchain?.let { "toolchain" to it },
            target?.let { "target" to it },
            default?.let { "default" to it.toString() },
            `override`?.let { "override" to it.toString() },
            profile?.let { "profile" to it },
            components?.let { "components" to it.joinToString(",") },
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
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

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
