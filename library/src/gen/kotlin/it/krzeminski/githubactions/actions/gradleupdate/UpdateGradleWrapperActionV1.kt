// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.gradleupdate

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Update Gradle Wrapper Action
 *
 * Keeps Gradle Wrapper script in your projects up-to-date
 *
 * https://github.com/gradle-update/update-gradle-wrapper-action
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
    public val pathsIgnore: List<String>? = null
) : Action("gradle-update", "update-gradle-wrapper-action", "v1") {
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
        ).toTypedArray()
    )
}
