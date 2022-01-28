package it.krzeminski.githubactions.actions.gradleupdate

import it.krzeminski.githubactions.actions.Action

@Suppress("LongParameterList")
class UpdateGradleWrapperActionV1(
    val repoToken: String? = null,
    val reviewers: List<String>? = null,
    val teamReviewers: List<String>? = null,
    val labels: List<String>? = null,
    val baseBranch: String? = null,
    val targetBranch: String? = null,
    val setDistributionChecksum: Boolean? = null,
    val paths: List<String>? = null,
    val pathsIgnore: List<String>? = null,
) : Action("gradle-update", "update-gradle-wrapper-action", "v1") {
    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
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
