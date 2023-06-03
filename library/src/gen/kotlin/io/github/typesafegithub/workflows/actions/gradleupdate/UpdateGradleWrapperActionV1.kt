// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.gradleupdate

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
 * Action: Update Gradle Wrapper Action
 *
 * Keeps Gradle Wrapper script in your projects up-to-date
 *
 * [Action on GitHub](https://github.com/gradle-update/update-gradle-wrapper-action)
 */
public data class UpdateGradleWrapperActionV1 private constructor(
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
     * Which merge method to use for auto-merge (either `MERGE`, `REBASE`, or `SQUASH`).  If unset,
     * auto-merge will not be enabled on opened PRs.
     */
    public val mergeMethod: UpdateGradleWrapperActionV1.MergeMethod? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("gradle-update", "update-gradle-wrapper-action", _customVersion ?:
        "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        repoToken: String? = null,
        reviewers: List<String>? = null,
        teamReviewers: List<String>? = null,
        labels: List<String>? = null,
        baseBranch: String? = null,
        targetBranch: String? = null,
        setDistributionChecksum: Boolean? = null,
        paths: List<String>? = null,
        pathsIgnore: List<String>? = null,
        releaseChannel: UpdateGradleWrapperActionV1.ReleaseChannel? = null,
        mergeMethod: UpdateGradleWrapperActionV1.MergeMethod? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(repoToken=repoToken, reviewers=reviewers, teamReviewers=teamReviewers, labels=labels,
            baseBranch=baseBranch, targetBranch=targetBranch,
            setDistributionChecksum=setDistributionChecksum, paths=paths, pathsIgnore=pathsIgnore,
            releaseChannel=releaseChannel, mergeMethod=mergeMethod, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
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
            mergeMethod?.let { "merge-method" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

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

    public sealed class MergeMethod(
        public val stringValue: String,
    ) {
        public object Merge : UpdateGradleWrapperActionV1.MergeMethod("MERGE")

        public object Rebase : UpdateGradleWrapperActionV1.MergeMethod("REBASE")

        public object Squash : UpdateGradleWrapperActionV1.MergeMethod("SQUASH")

        public class Custom(
            customStringValue: String,
        ) : UpdateGradleWrapperActionV1.MergeMethod(customStringValue)
    }
}
