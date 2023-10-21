// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.mikas

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class KoverReportV1 private constructor(
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
     * Report counter type (`INSTRUCTION``, `LINE` or `BRANCH`) to calculate coverage metrics.
     */
    public val coverageCounterType: KoverReportV1.CoverageCounterType? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<KoverReportV1.Outputs>("mi-kas", "kover-report", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        path: String,
        token: String,
        title: String? = null,
        updateComment: Boolean? = null,
        minCoverageOverall: Int? = null,
        minCoverageChangedFiles: Int? = null,
        coverageCounterType: KoverReportV1.CoverageCounterType? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(path=path, token=token, title=title, updateComment=updateComment,
            minCoverageOverall=minCoverageOverall, minCoverageChangedFiles=minCoverageChangedFiles,
            coverageCounterType=coverageCounterType, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "path" to path,
            "token" to token,
            title?.let { "title" to it },
            updateComment?.let { "update-comment" to it.toString() },
            minCoverageOverall?.let { "min-coverage-overall" to it.toString() },
            minCoverageChangedFiles?.let { "min-coverage-changed-files" to it.toString() },
            coverageCounterType?.let { "coverage-counter-type" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class CoverageCounterType(
        public val stringValue: String,
    ) {
        public object Instruction : KoverReportV1.CoverageCounterType("INSTRUCTION")

        public object Line : KoverReportV1.CoverageCounterType("LINE")

        public object Branch : KoverReportV1.CoverageCounterType("BRANCH")

        public class Custom(
            customStringValue: String,
        ) : KoverReportV1.CoverageCounterType(customStringValue)
    }

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
