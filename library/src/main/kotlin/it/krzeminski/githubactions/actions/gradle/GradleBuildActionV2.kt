package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action

@Suppress("LongParameterList")
class GradleBuildActionV2(
    val gradleVersion: String? = null,
    val cacheDisabled: Boolean? = null,
    val cacheReadOnly: Boolean? = null,
    val gradleHomeCacheIncludes: List<String>? = null,
    val gradleHomeCacheExcludes: List<String>? = null,
    val arguments: String? = null,
    val buildRootDirectory: String? = null,
    val gradleExecutable: String? = null,
) : Action("gradle", "gradle-build-action", "v2") {
    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            gradleVersion?.let { "gradle-version" to it },
            cacheDisabled?.let { "cache-disabled" to it.toString() },
            cacheReadOnly?.let { "cache-read-only" to it.toString() },
            gradleHomeCacheIncludes?.let { "gradle-home-cache-includes" to it.joinToString("\n") },
            gradleHomeCacheExcludes?.let { "gradle-home-cache-excludes" to it.joinToString("\n") },
            arguments?.let { "arguments" to it },
            buildRootDirectory?.let { "build-root-directory" to it },
            gradleExecutable?.let { "gradle-executable" to it },
        ).toTypedArray()
    )
}
