// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.coverallsapp

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Coveralls GitHub Action
 *
 * Send test coverage data to Coveralls.io for analysis, change tracking, and notifications.
 *
 * [Action on GitHub](https://github.com/coverallsapp/github-action)
 */
public class GithubActionV1(
    public val githubToken: String,
    /**
     * Path to lcov file
     */
    public val pathToLcov: String? = null,
    /**
     * Job flag name, e.g. "Unit", "Functional", or "Integration". Will be shown in the Coveralls
     * UI.
     */
    public val flagName: String? = null,
    /**
     * Set to true if you are running parallel jobs, then use "parallel_finished: true" for the last
     * action.
     */
    public val parallel: Boolean? = null,
    /**
     * Set to true for the last action when using "parallel: true".
     */
    public val parallelFinished: Boolean? = null,
    /**
     * Coveralls Enterprise server (more info: https://enterprise.coveralls.io)
     */
    public val coverallsEndpoint: String? = null,
    /**
     * The root folder of the project that originally ran the tests
     */
    public val basePath: String? = null,
    /**
     * Override the branch name
     */
    public val gitBranch: String? = null,
    /**
     * Override the commit sha
     */
    public val gitCommit: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<GithubActionV1.Outputs>(
    "coverallsapp", "github-action",
    _customVersion
        ?: "1.1.3"
) {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "github-token" to githubToken,
            pathToLcov?.let { "path-to-lcov" to it },
            flagName?.let { "flag-name" to it },
            parallel?.let { "parallel" to it.toString() },
            parallelFinished?.let { "parallel-finished" to it.toString() },
            coverallsEndpoint?.let { "coveralls-endpoint" to it },
            basePath?.let { "base-path" to it },
            gitBranch?.let { "git-branch" to it },
            gitCommit?.let { "git-commit" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Result status of Coveralls API post.
         */
        public val coverallsApiResult: String = "steps.$stepId.outputs.coveralls-api-result"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
