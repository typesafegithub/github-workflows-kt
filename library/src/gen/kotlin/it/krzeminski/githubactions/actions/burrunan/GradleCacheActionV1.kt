// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.burrunan

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
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
 * Action: Gradle Cache
 *
 * Caches .gradle folder (dependencies, local build cache, ...)
 *
 * [Action on GitHub](https://github.com/burrunan/gradle-cache-action)
 */
public data class GradleCacheActionV1 private constructor(
    /**
     * A job identifier to avoid cache pollution from different jobs
     */
    public val jobId: String? = null,
    /**
     * Relative path under $GITHUB_WORKSPACE where Git repository is placed
     */
    public val buildRootDirectory: String? = null,
    /**
     * Overrides the location of $HOME (e.g. to avoid use of /root when running in Docker)
     */
    public val homeDirectory: String? = null,
    /**
     * (wrapper | or explicit version) Caches often depend on the Gradle version, so this parameter
     * sets the ID to use for cache keys. It does not affect the Gradle version used for build
     */
    public val gradleVersion: String? = null,
    /**
     * Configures caches for read-only opreration (e.g. to save GitHub Actions storage limit)
     */
    public val readOnly: Boolean? = null,
    /**
     * Enables caching of $HOME/.gradle/caches/&#42;.&#42;/generated-gradle-jars
     */
    public val saveGeneratedGradleJars: Boolean? = null,
    /**
     * Enables caching of $HOME/.gradle/caches/build-cache-1
     */
    public val saveLocalBuildCache: Boolean? = null,
    /**
     * Adds com.github.burrunan.multi-cache plugin to settings.gradle so GitHub Actions cache can be
     * used in parallel with Gradle remote build cache
     */
    public val multiCacheEnabled: Boolean? = null,
    /**
     * Configures com.github.burrunan.multi-cache version to use
     */
    public val multiCacheVersion: String? = null,
    /**
     * Configures repository where com.github.burrunan.multi-cache can be located
     */
    public val multiCacheRepository: String? = null,
    /**
     * Configures group id for selecting only com.github.burrunan.multi-cache artifacts (it enables
     * Gradle to use custom repository for multi-cache only)
     */
    public val multiCacheGroupIdFilter: String? = null,
    /**
     * Enables caching of ~/.gradle/caches/modules-*
     */
    public val saveGradleDependenciesCache: Boolean? = null,
    /**
     * Activates only the caches that are relevant for executing gradle command.
     * This is helpful when build job executes multiple gradle commands sequentially.
     * Then the caching is implemented in the very first one, and the subsequent should be marked
     * with execution-only-caches: true
     */
    public val executionOnlyCaches: Boolean? = null,
    /**
     * Activates a remote cache that proxies requests to GitHub Actions cache
     */
    public val remoteBuildCacheProxyEnabled: Boolean? = null,
    /**
     * Extra files to take into account for ~/.gradle/caches dependencies
     */
    public val gradleDependenciesCacheKey: List<String>? = null,
    /**
     * Enables caching of ~/.m2/repository/
     */
    public val saveMavenDependenciesCache: Boolean? = null,
    /**
     * Specifies ignored paths in the Maven Local repository (e.g. the artifacts of the current
     * project)
     */
    public val mavenLocalIgnorePaths: List<String>? = null,
    /**
     * Shows extra logging to debug the action
     */
    public val debug: Boolean? = null,
    /**
     * Enables concurent cache download and upload (disabled by default for better log output)
     */
    public val concurrent: Boolean? = null,
    /**
     * Gradle arguments to pass (optionally multiline)
     */
    public val arguments: List<String>? = null,
    /**
     * Extra Gradle properties (multiline) which would be passed as -Pname=value arguments
     */
    public val properties: List<String>? = null,
    /**
     * Publishes Gradle Build Scan URL to job report.
     */
    public val gradleBuildScanReport: Boolean? = null,
    /**
     * Enables warning when distributionSha256Sum property is missing in gradle-wrapper.properties
     */
    public val gradleDistributionSha256SumWarning: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<GradleCacheActionV1.Outputs>("burrunan", "gradle-cache-action", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        jobId: String? = null,
        buildRootDirectory: String? = null,
        homeDirectory: String? = null,
        gradleVersion: String? = null,
        readOnly: Boolean? = null,
        saveGeneratedGradleJars: Boolean? = null,
        saveLocalBuildCache: Boolean? = null,
        multiCacheEnabled: Boolean? = null,
        multiCacheVersion: String? = null,
        multiCacheRepository: String? = null,
        multiCacheGroupIdFilter: String? = null,
        saveGradleDependenciesCache: Boolean? = null,
        executionOnlyCaches: Boolean? = null,
        remoteBuildCacheProxyEnabled: Boolean? = null,
        gradleDependenciesCacheKey: List<String>? = null,
        saveMavenDependenciesCache: Boolean? = null,
        mavenLocalIgnorePaths: List<String>? = null,
        debug: Boolean? = null,
        concurrent: Boolean? = null,
        arguments: List<String>? = null,
        properties: List<String>? = null,
        gradleBuildScanReport: Boolean? = null,
        gradleDistributionSha256SumWarning: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(jobId=jobId, buildRootDirectory=buildRootDirectory, homeDirectory=homeDirectory,
            gradleVersion=gradleVersion, readOnly=readOnly,
            saveGeneratedGradleJars=saveGeneratedGradleJars,
            saveLocalBuildCache=saveLocalBuildCache, multiCacheEnabled=multiCacheEnabled,
            multiCacheVersion=multiCacheVersion, multiCacheRepository=multiCacheRepository,
            multiCacheGroupIdFilter=multiCacheGroupIdFilter,
            saveGradleDependenciesCache=saveGradleDependenciesCache,
            executionOnlyCaches=executionOnlyCaches,
            remoteBuildCacheProxyEnabled=remoteBuildCacheProxyEnabled,
            gradleDependenciesCacheKey=gradleDependenciesCacheKey,
            saveMavenDependenciesCache=saveMavenDependenciesCache,
            mavenLocalIgnorePaths=mavenLocalIgnorePaths, debug=debug, concurrent=concurrent,
            arguments=arguments, properties=properties, gradleBuildScanReport=gradleBuildScanReport,
            gradleDistributionSha256SumWarning=gradleDistributionSha256SumWarning,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            jobId?.let { "job-id" to it },
            buildRootDirectory?.let { "build-root-directory" to it },
            homeDirectory?.let { "home-directory" to it },
            gradleVersion?.let { "gradle-version" to it },
            readOnly?.let { "read-only" to it.toString() },
            saveGeneratedGradleJars?.let { "save-generated-gradle-jars" to it.toString() },
            saveLocalBuildCache?.let { "save-local-build-cache" to it.toString() },
            multiCacheEnabled?.let { "multi-cache-enabled" to it.toString() },
            multiCacheVersion?.let { "multi-cache-version" to it },
            multiCacheRepository?.let { "multi-cache-repository" to it },
            multiCacheGroupIdFilter?.let { "multi-cache-group-id-filter" to it },
            saveGradleDependenciesCache?.let { "save-gradle-dependencies-cache" to it.toString() },
            executionOnlyCaches?.let { "execution-only-caches" to it.toString() },
            remoteBuildCacheProxyEnabled?.let { "remote-build-cache-proxy-enabled" to it.toString()
                    },
            gradleDependenciesCacheKey?.let { "gradle-dependencies-cache-key" to
                    it.joinToString("\n") },
            saveMavenDependenciesCache?.let { "save-maven-dependencies-cache" to it.toString() },
            mavenLocalIgnorePaths?.let { "maven-local-ignore-paths" to it.joinToString("\n") },
            debug?.let { "debug" to it.toString() },
            concurrent?.let { "concurrent" to it.toString() },
            arguments?.let { "arguments" to it.joinToString("\n") },
            properties?.let { "properties" to it.joinToString("\n") },
            gradleBuildScanReport?.let { "gradle-build-scan-report" to it.toString() },
            gradleDistributionSha256SumWarning?.let { "gradle-distribution-sha-256-sum-warning" to
                    it.toString() },
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
