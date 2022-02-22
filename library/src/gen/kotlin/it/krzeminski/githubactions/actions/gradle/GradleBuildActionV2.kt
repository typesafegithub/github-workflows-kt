// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Gradle Build Action
 *
 * Configures Gradle for use in GitHub actions, caching useful state in the GitHub actions cache
 *
 * [Action on GitHub](https://github.com/gradle/gradle-build-action)
 */
public class GradleBuildActionV2(
    /**
     * Gradle version to use
     */
    public val gradleVersion: String? = null,
    /**
     * When 'true', all caching is disabled. No entries will be written to or read from the cache.
     */
    public val cacheDisabled: Boolean? = null,
    /**
     * When 'true', existing entries will be read from the cache but no entries will be written.
     */
    public val cacheReadOnly: Boolean? = null,
    /**
     * Paths within Gradle User Home to cache.
     */
    public val gradleHomeCacheIncludes: List<String>? = null,
    /**
     * Paths within Gradle User Home to exclude from cache.
     */
    public val gradleHomeCacheExcludes: List<String>? = null,
    /**
     * Gradle command line arguments (supports multi-line input)
     */
    public val arguments: String? = null,
    /**
     * Path to the root directory of the build
     */
    public val buildRootDirectory: String? = null,
    /**
     * Path to the Gradle executable
     */
    public val gradleExecutable: String? = null,
    /**
     * When 'true', entries will not be restored from the cache but will be saved at the end of the
     * Job. This allows a 'clean' cache entry to be written.
     */
    public val cacheWriteOnly: String? = null,
    /**
     * When 'true', the action will not attempt to restore the Gradle User Home entries from other
     * Jobs.
     */
    public val gradleHomeCacheStrictMatch: String? = null,
    /**
     * Used to uniquely identify the current job invocation. Defaults to the matrix values for this
     * job; this should not be overridden by users (INTERNAL).
     */
    public val workflowJobContext: String? = null
) : Action("gradle", "gradle-build-action", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            gradleVersion?.let { "gradle-version" to it },
            cacheDisabled?.let { "cache-disabled" to it.toString() },
            cacheReadOnly?.let { "cache-read-only" to it.toString() },
            gradleHomeCacheIncludes?.let { "gradle-home-cache-includes" to it.joinToString("\n") },
            gradleHomeCacheExcludes?.let { "gradle-home-cache-excludes" to it.joinToString("\n") },
            arguments?.let { "arguments" to it },
            buildRootDirectory?.let { "build-root-directory" to it },
            gradleExecutable?.let { "gradle-executable" to it },
            cacheWriteOnly?.let { "cache-write-only" to it },
            gradleHomeCacheStrictMatch?.let { "gradle-home-cache-strict-match" to it },
            workflowJobContext?.let { "workflow-job-context" to it },
        ).toTypedArray()
    )

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Link to the build scan if any
         */
        public val buildScanUrl: String = "steps.$stepId.outputs.build-scan-url"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
