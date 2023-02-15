// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.mikas

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Kotlinx Kover Report
 *
 * Github action that publishes the Kover code coverage report as a comment in pull requests
 *
 * [Action on GitHub](https://github.com/mi-kas/kover-report)
 */
public data class KoverReportV1(
    /**
     * Path to the generated kover report xml file
     */
    public val path: String,
    /**
     * Github personal token to add commits to the pull request
     */
    public val token: String,
    /**
     * Title for the pull request comment
     */
    public val title: String? = null,
    /**
     * Update the coverage report comment instead of creating a new one. Requires title to be set.
     */
    public val updateComment: Boolean? = null,
    /**
     * The minimum code coverage that is required to pass for overall project
     */
    public val minCoverageOverall: Int? = null,
    /**
     * The minimum code coverage that is required to pass for changed files
     */
    public val minCoverageChangedFiles: Int? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<KoverReportV1.Outputs>("mi-kas", "kover-report", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "path" to path,
            "token" to token,
            title?.let { "title" to it },
            updateComment?.let { "update-comment" to it.toString() },
            minCoverageOverall?.let { "min-coverage-overall" to it.toString() },
            minCoverageChangedFiles?.let { "min-coverage-changed-files" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The overall coverage of the project
         */
        public val coverageOverall: String = "steps.$stepId.outputs.coverage-overall"

        /**
         * The total coverage of all changed files
         */
        public val coverageChangedFiles: String = "steps.$stepId.outputs.coverage-changed-files"
    }
}
