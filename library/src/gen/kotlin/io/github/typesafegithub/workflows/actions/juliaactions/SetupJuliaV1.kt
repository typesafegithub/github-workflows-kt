// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.juliaactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup Julia environment
 *
 * Setup a Julia environment and add it to the PATH
 *
 * [Action on GitHub](https://github.com/julia-actions/setup-julia)
 */
public data class SetupJuliaV1 private constructor(
    /**
     * The Julia version to download (if necessary) and use. Example: 1.0.4
     */
    public val version: String? = null,
    /**
     * Include prereleases when matching the Julia version to available versions.
     */
    public val includeAllPrereleases: Boolean? = null,
    /**
     * Architecture of the Julia binaries. Defaults to the architecture of the runner executing the
     * job.
     */
    public val arch: SetupJuliaV1.Architecture? = null,
    /**
     * Display InteractiveUtils.versioninfo() after installing
     */
    public val showVersioninfo: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupJuliaV1.Outputs>("julia-actions", "setup-julia", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        version: String? = null,
        includeAllPrereleases: Boolean? = null,
        arch: SetupJuliaV1.Architecture? = null,
        showVersioninfo: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(version=version, includeAllPrereleases=includeAllPrereleases, arch=arch,
            showVersioninfo=showVersioninfo, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            version?.let { "version" to it },
            includeAllPrereleases?.let { "include-all-prereleases" to it.toString() },
            arch?.let { "arch" to it.stringValue },
            showVersioninfo?.let { "show-versioninfo" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X64 : SetupJuliaV1.Architecture("x64")

        public object X86 : SetupJuliaV1.Architecture("x86")

        public object Aarch64 : SetupJuliaV1.Architecture("aarch64")

        public class Custom(
            customStringValue: String,
        ) : SetupJuliaV1.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The installed Julia version. May vary from the version input if a version range was given
         * as input.
         */
        public val juliaVersion: String = "steps.$stepId.outputs.julia-version"

        /**
         * Path to the directory containing the Julia executable. Equivalent to JULIA_BINDIR:
         * https://docs.julialang.org/en/v1/manual/environment-variables/#JULIA_BINDIR
         */
        public val juliaBindir: String = "steps.$stepId.outputs.julia-bindir"
    }
}
