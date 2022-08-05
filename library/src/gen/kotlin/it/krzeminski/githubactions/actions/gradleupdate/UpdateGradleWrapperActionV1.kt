// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.gradleupdate

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Update Gradle Wrapper Action
 *
 * Keeps Gradle Wrapper script in your projects up-to-date
 *
 * [Action on GitHub](https://github.com/gradle-update/update-gradle-wrapper-action)
 */
public class UpdateGradleWrapperActionV1(
    /**
     * Access token for the repository, e.g. `{{ secrets.GITHUB_TOKEN }}`.
     */
    public val repoToken: String? = null,
    /**
     * List of users to request a review from (comma or newline-separated).
     */
    public val reviewers: List<String>? = null,
    /**
     * List of teams to request a review from (comma or newline-separated).
     */
    public val teamReviewers: List<String>? = null,
    /**
     * List of labels to set on the Pull Request (comma or newline-separated).
     */
    public val labels: List<String>? = null,
    /**
     * Base branch where the action will run and update the Gradle Wrapper.
     */
    public val baseBranch: String? = null,
    /**
     * Branch to create the Pull Request against.
     */
    public val targetBranch: String? = null,
    /**
     * Whether to set the `distributionSha256Sum` property in `gradle-wrapper.properties`.
     */
    public val setDistributionChecksum: Boolean? = null,
    /**
     * List of paths where to search for Gradle Wrapper files (comma or newline-separated).
     */
    public val paths: List<String>? = null,
    /**
     * List of paths to be excluded when searching for Gradle Wrapper files (comma or
     * newline-separated).
     */
    public val pathsIgnore: List<String>? = null,
    /**
     * Gradle release channel to be used (either `stable` or `release-candidate`).
     */
    public val releaseChannel: UpdateGradleWrapperActionV1.ReleaseChannel? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("gradle-update", "update-gradle-wrapper-action", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            repoToken?.let { "repo-token" to it },
            reviewers?.let { "reviewers" to it.joinToString(",") },
            teamReviewers?.let { "team-reviewers" to it.joinToString(",") },
            labels?.let { "labels" to it.joinToString(",") },
            baseBranch?.let { "base-branch" to it },
            targetBranch?.let { "target-branch" to it },
            setDistributionChecksum?.let { "set-distribution-checksum" to it.toString() },
            paths?.let { "paths" to it.joinToString(",") },
            pathsIgnore?.let { "paths-ignore" to it.joinToString(",") },
            releaseChannel?.let { "release-channel" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class ReleaseChannel(
        public val stringValue: String,
    ) {
        public object Stable : UpdateGradleWrapperActionV1.ReleaseChannel("stable")

        public object ReleaseCandidate :
                UpdateGradleWrapperActionV1.ReleaseChannel("release-candidate")

        public class Custom(
            customStringValue: String,
        ) : UpdateGradleWrapperActionV1.ReleaseChannel(customStringValue)
    }
}
