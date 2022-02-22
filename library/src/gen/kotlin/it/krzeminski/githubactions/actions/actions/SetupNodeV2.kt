// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Setup Node.js environment
 *
 * Setup a Node.js environment by adding problem matchers and optionally downloading and adding it
 * to the PATH
 *
 * [Action on GitHub](https://github.com/actions/setup-node)
 */
public class SetupNodeV2(
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
    public val cache: SetupNodeV2.PackageManager? = null,
    /**
     * Used to specify the path to a dependency file: package-lock.json, yarn.lock, etc. Supports
     * wildcards or a list of file names for caching multiple dependencies.
     */
    public val cacheDependencyPath: List<String>? = null,
    /**
     * Deprecated. Use node-version instead. Will not be supported after October 1, 2019
     */
    @Deprecated("The version property will not be supported after October 1, 2019. Use node-version instead")
    public val version: String? = null
) : ActionWithOutputs<SetupNodeV2.Outputs>("actions", "setup-node", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
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
            version?.let { "version" to it },
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public sealed class PackageManager(
        public val stringValue: String
    ) {
        public object Npm : SetupNodeV2.PackageManager("npm")

        public object Yarn : SetupNodeV2.PackageManager("yarn")

        public object Pnpm : SetupNodeV2.PackageManager("pnpm")

        public class Custom(
            customStringValue: String
        ) : SetupNodeV2.PackageManager(customStringValue)
    }

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * A boolean value to indicate if a cache was hit
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
