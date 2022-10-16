// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup Go environment
 *
 * Setup a Go environment and add it to the PATH
 *
 * [Action on GitHub](https://github.com/actions/setup-go)
 */
public class SetupGoV3(
    /**
     * The Go version to download (if necessary) and use. Supports semver spec and ranges.
     */
    public val goVersion: String? = null,
    /**
     * Path to the go.mod file.
     */
    public val goVersionFile: String? = null,
    /**
     * Set this option to true if you want the action to always check for the latest available
     * version that satisfies the version spec
     */
    public val checkLatest: Boolean? = null,
    /**
     * Used to pull node distributions from go-versions.  Since there's a default, this is typically
     * not supplied by the user.
     */
    public val token: String? = null,
    /**
     * Used to specify whether caching is needed. Set to true, if you'd like to enable caching.
     */
    public val cache: Boolean? = null,
    /**
     * Used to specify the path to a dependency file - go.sum
     */
    public val cacheDependencyPath: String? = null,
    /**
     * Target architecture for Go to use. Examples: x86, x64. Will use system architecture by
     * default.
     */
    public val architecture: SetupGoV3.Architecture? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SetupGoV3.Outputs>("actions", "setup-go", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            goVersion?.let { "go-version" to it },
            goVersionFile?.let { "go-version-file" to it },
            checkLatest?.let { "check-latest" to it.toString() },
            token?.let { "token" to it },
            cache?.let { "cache" to it.toString() },
            cacheDependencyPath?.let { "cache-dependency-path" to it },
            architecture?.let { "architecture" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X86 : SetupGoV3.Architecture("x86")

        public object X64 : SetupGoV3.Architecture("x64")

        public class Custom(
            customStringValue: String,
        ) : SetupGoV3.Architecture(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The installed Go version. Useful when given a version range as input.
         */
        public val goVersion: String = "steps.$stepId.outputs.go-version"

        /**
         * A boolean value to indicate if a cache was hit
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
