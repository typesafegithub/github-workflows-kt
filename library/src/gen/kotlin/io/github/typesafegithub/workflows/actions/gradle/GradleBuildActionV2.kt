// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.gradle

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
 * Action: Gradle Build Action
 *
 * Configures Gradle for use in GitHub actions, caching useful state in the GitHub actions cache
 *
 * [Action on GitHub](https://github.com/gradle/gradle-build-action)
 */
public data class GradleBuildActionV2 private constructor(
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
     * By default this value is 'false' for workflows on the GitHub default branch and 'true' for
     * workflows on other branches.
     */
    public val cacheReadOnly: Boolean? = null,
    /**
     * When 'true', entries will not be restored from the cache but will be saved at the end of the
     * Job.
     * Setting this to 'true' implies cache-read-only will be 'false'.
     */
    public val cacheWriteOnly: Boolean? = null,
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
     * When 'false', no Job Summary will be generated for the Job.
     */
    public val generateJobSummary: Boolean? = null,
    /**
     * When 'true', the action will not attempt to restore the Gradle User Home entries from other
     * Jobs.
     */
    public val gradleHomeCacheStrictMatch: String? = null,
    /**
     * Used to uniquely identify the current job invocation. Defaults to the matrix values for this
     * job; this should not be overridden by users (INTERNAL).
     */
    public val workflowJobContext: String? = null,
    /**
     * When 'true', the action will attempt to remove any stale/unused entries from the Gradle User
     * Home prior to saving to the GitHub Actions cache.
     */
    public val gradleHomeCacheCleanup: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<GradleBuildActionV2.Outputs>("gradle", "gradle-build-action", _customVersion ?:
        "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        gradleVersion: String? = null,
        cacheDisabled: Boolean? = null,
        cacheReadOnly: Boolean? = null,
        cacheWriteOnly: Boolean? = null,
        gradleHomeCacheIncludes: List<String>? = null,
        gradleHomeCacheExcludes: List<String>? = null,
        arguments: String? = null,
        buildRootDirectory: String? = null,
        gradleExecutable: String? = null,
        generateJobSummary: Boolean? = null,
        gradleHomeCacheStrictMatch: String? = null,
        workflowJobContext: String? = null,
        gradleHomeCacheCleanup: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(gradleVersion=gradleVersion, cacheDisabled=cacheDisabled, cacheReadOnly=cacheReadOnly,
            cacheWriteOnly=cacheWriteOnly, gradleHomeCacheIncludes=gradleHomeCacheIncludes,
            gradleHomeCacheExcludes=gradleHomeCacheExcludes, arguments=arguments,
            buildRootDirectory=buildRootDirectory, gradleExecutable=gradleExecutable,
            generateJobSummary=generateJobSummary,
            gradleHomeCacheStrictMatch=gradleHomeCacheStrictMatch,
            workflowJobContext=workflowJobContext, gradleHomeCacheCleanup=gradleHomeCacheCleanup,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            gradleVersion?.let { "gradle-version" to it },
            cacheDisabled?.let { "cache-disabled" to it.toString() },
            cacheReadOnly?.let { "cache-read-only" to it.toString() },
            cacheWriteOnly?.let { "cache-write-only" to it.toString() },
            gradleHomeCacheIncludes?.let { "gradle-home-cache-includes" to it.joinToString("\n") },
            gradleHomeCacheExcludes?.let { "gradle-home-cache-excludes" to it.joinToString("\n") },
            arguments?.let { "arguments" to it },
            buildRootDirectory?.let { "build-root-directory" to it },
            gradleExecutable?.let { "gradle-executable" to it },
            generateJobSummary?.let { "generate-job-summary" to it.toString() },
            gradleHomeCacheStrictMatch?.let { "gradle-home-cache-strict-match" to it },
            workflowJobContext?.let { "workflow-job-context" to it },
            gradleHomeCacheCleanup?.let { "gradle-home-cache-cleanup" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Link to the build scan if any
         */
        public val buildScanUrl: String = "steps.$stepId.outputs.build-scan-url"
    }
}
