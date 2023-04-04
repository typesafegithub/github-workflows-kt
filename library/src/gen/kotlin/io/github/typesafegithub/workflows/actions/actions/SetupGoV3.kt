// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
@Deprecated(
    message = "This action has a newer major version: SetupGoV4",
    replaceWith = ReplaceWith("SetupGoV4"),
)
public data class SetupGoV3 private constructor(
    /**
     * The Go version to download (if necessary) and use. Supports semver spec and ranges.
     */
    public val goVersion: String? = null,
    /**
     * Path to the go.mod or go.work file.
     */
    public val goVersionFile: String? = null,
    /**
     * Set this option to true if you want the action to always check for the latest available
     * version that satisfies the version spec
     */
    public val checkLatest: Boolean? = null,
    /**
     * Used to pull node distributions from go-versions. Since there's a default, this is typically
     * not supplied by the user. When running this action on github.com, the default value is
     * sufficient. When running on GHES, you can pass a personal access token for github.com if you are
     * experiencing rate limiting.
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
    public val _customVersion: String? = null,
) : Action<SetupGoV3.Outputs>("actions", "setup-go", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        goVersion: String? = null,
        goVersionFile: String? = null,
        checkLatest: Boolean? = null,
        token: String? = null,
        cache: Boolean? = null,
        cacheDependencyPath: String? = null,
        architecture: SetupGoV3.Architecture? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(goVersion=goVersion, goVersionFile=goVersionFile, checkLatest=checkLatest, token=token,
            cache=cache, cacheDependencyPath=cacheDependencyPath, architecture=architecture,
            _customInputs=_customInputs, _customVersion=_customVersion)

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
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The installed Go version. Useful when given a version range as input.
         */
        public val goVersion: String = "steps.$stepId.outputs.go-version"

        /**
         * A boolean value to indicate if a cache was hit
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"
    }
}
