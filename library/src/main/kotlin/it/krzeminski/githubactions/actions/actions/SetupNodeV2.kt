@file:Suppress("RedundantVisibilityModifier")

package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action Setup Node.js environment
 *
 * Setup a Node.js environment by adding problem matchers and optionally downloading and adding it
 * to the PATH
 *
 * http://github.com/actions/setup-node
 */
public data class SetupNodeV2(
    /**
     * Set always-auth in npmrc
     */
    public val alwaysAuth: Boolean? = null,
    /**
     * Version Spec of the version to use.  Examples: 12.x, 10.15.1, >=10.15.0
     */
    public val nodeVersion: String? = null,
    /**
     * File containing the version Spec of the version to use.  Examples: .nvmrc, .node-version
     */
    public val nodeVersionFile: String? = null,
    /**
     * Target architecture for Node to use. Examples: x86, x64. Will use system architecture by
     * default.
     */
    public val architecture: String? = null,
    /**
     * Set this option if you want the action to check for the latest available version that
     * satisfies the version spec
     */
    public val checkLatest: Boolean? = null,
    /**
     * Optional registry to set up for auth. Will set the registry in a project level .npmrc and
     * .yarnrc file, and set up auth to read in from env.NODE_AUTH_TOKEN
     */
    public val registryUrl: String? = null,
    /**
     * Optional scope for authenticating against scoped registries
     */
    public val scope: String? = null,
    /**
     * Used to pull node distributions from node-versions.  Since there's a default, this is
     * typically not supplied by the user.
     */
    public val token: String? = null,
    /**
     * Used to specify a package manager for caching in the default directory. Supported values:
     * npm, yarn, pnpm
     */
    public val cache: String? = null,
    /**
     * Used to specify the path to a dependency file: package-lock.json, yarn.lock, etc. Supports
     * wildcards or a list of file names for caching multiple dependencies.
     */
    public val cacheDependencyPath: String? = null,
    /**
     * Deprecated. Use node-version instead. Will not be supported after October 1, 2019
     */
    public val version: String? = null
) : Action("actions", "setup-node", "v2") {
    public override fun toYamlArguments(): LinkedHashMap<String, String> = yamlOf(
        "always-auth" to alwaysAuth,
        "node-version" to nodeVersion,
        "node-version-file" to nodeVersionFile,
        "architecture" to architecture,
        "check-latest" to checkLatest,
        "registry-url" to registryUrl,
        "scope" to scope,
        "token" to token,
        "cache" to cache,
        "cache-dependency-path" to cacheDependencyPath,
        "version" to version,
    )

    internal companion object {
        public val example_full_action: SetupNodeV2 = SetupNodeV2(
            alwaysAuth = false,
            nodeVersion = "node-version",
            nodeVersionFile = "node-version-file",
            architecture = "architecture",
            checkLatest = false,
            registryUrl = "registry-url",
            scope = "scope",
            token = "github.token",
            cache = "cache",
            cacheDependencyPath = "cache-dependency-path",
            version = "version",
        )

        public val example_full_map: Map<String, String> = mapOf(
            "always-auth" to "false",
            "node-version" to "node-version",
            "node-version-file" to "node-version-file",
            "architecture" to "architecture",
            "check-latest" to "false",
            "registry-url" to "registry-url",
            "scope" to "scope",
            "token" to "github.token",
            "cache" to "cache",
            "cache-dependency-path" to "cache-dependency-path",
            "version" to "version",
        )
    }
}
