// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

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
 * Action: Setup Node.js environment
 *
 * Setup a Node.js environment by adding problem matchers and optionally downloading and adding it
 * to the PATH.
 *
 * [Action on GitHub](https://github.com/actions/setup-node)
 */
public data class SetupNodeV3 private constructor(
    /**
     * Set always-auth in npmrc.
     */
    public val alwaysAuth: Boolean? = null,
    /**
     * Version Spec of the version to use. Examples: 12.x, 10.15.1, >=10.15.0.
     */
    public val nodeVersion: String? = null,
    /**
     * File containing the version Spec of the version to use.  Examples: .nvmrc, .node-version,
     * .tool-versions.
     */
    public val nodeVersionFile: String? = null,
    /**
     * Target architecture for Node to use. Examples: x86, x64. Will use system architecture by
     * default.
     */
    public val architecture: String? = null,
    /**
     * Set this option if you want the action to check for the latest available version that
     * satisfies the version spec.
     */
    public val checkLatest: Boolean? = null,
    /**
     * Optional registry to set up for auth. Will set the registry in a project level .npmrc and
     * .yarnrc file, and set up auth to read in from env.NODE_AUTH_TOKEN.
     */
    public val registryUrl: String? = null,
    /**
     * Optional scope for authenticating against scoped registries. Will fall back to the repository
     * owner when using the GitHub Packages registry (https://npm.pkg.github.com/).
     */
    public val scope: String? = null,
    /**
     * Used to pull node distributions from node-versions. Since there's a default, this is
     * typically not supplied by the user. When running this action on github.com, the default value is
     * sufficient. When running on GHES, you can pass a personal access token for github.com if you are
     * experiencing rate limiting.
     */
    public val token: String? = null,
    /**
     * Used to specify a package manager for caching in the default directory. Supported values:
     * npm, yarn, pnpm.
     */
    public val cache: SetupNodeV3.PackageManager? = null,
    /**
     * Used to specify the path to a dependency file: package-lock.json, yarn.lock, etc. Supports
     * wildcards or a list of file names for caching multiple dependencies.
     */
    public val cacheDependencyPath: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupNodeV3.Outputs>("actions", "setup-node", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        alwaysAuth: Boolean? = null,
        nodeVersion: String? = null,
        nodeVersionFile: String? = null,
        architecture: String? = null,
        checkLatest: Boolean? = null,
        registryUrl: String? = null,
        scope: String? = null,
        token: String? = null,
        cache: SetupNodeV3.PackageManager? = null,
        cacheDependencyPath: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(alwaysAuth=alwaysAuth, nodeVersion=nodeVersion, nodeVersionFile=nodeVersionFile,
            architecture=architecture, checkLatest=checkLatest, registryUrl=registryUrl,
            scope=scope, token=token, cache=cache, cacheDependencyPath=cacheDependencyPath,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            alwaysAuth?.let { "always-auth" to it.toString() },
            nodeVersion?.let { "node-version" to it },
            nodeVersionFile?.let { "node-version-file" to it },
            architecture?.let { "architecture" to it },
            checkLatest?.let { "check-latest" to it.toString() },
            registryUrl?.let { "registry-url" to it },
            scope?.let { "scope" to it },
            token?.let { "token" to it },
            cache?.let { "cache" to it.stringValue },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class PackageManager(
        public val stringValue: String,
    ) {
        public object Npm : SetupNodeV3.PackageManager("npm")

        public object Yarn : SetupNodeV3.PackageManager("yarn")

        public object Pnpm : SetupNodeV3.PackageManager("pnpm")

        public class Custom(
            customStringValue: String,
        ) : SetupNodeV3.PackageManager(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A boolean value to indicate if a cache was hit.
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"

        /**
         * The installed node version.
         */
        public val nodeVersion: String = "steps.$stepId.outputs.node-version"
    }
}
