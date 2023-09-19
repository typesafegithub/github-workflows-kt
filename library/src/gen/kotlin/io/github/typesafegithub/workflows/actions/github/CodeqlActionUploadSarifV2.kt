// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.github

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Code Scanning : Upload SARIF
 *
 * Upload the analysis results
 *
 * [Action on GitHub](https://github.com/github/codeql-action/tree/v2/upload-sarif)
 */
public data class CodeqlActionUploadSarifV2 private constructor(
    /**
     * The SARIF file or directory of SARIF files to be uploaded to GitHub code scanning.
     * See
     * https://docs.github.com/en/code-security/code-scanning/integrating-with-code-scanning/uploading-a-sarif-file-to-github#uploading-a-code-scanning-analysis-with-github-actions
     * for information on the maximum number of results and maximum file size supported by code
     * scanning.
     */
    public val sarifFile: String? = null,
    /**
     * The path at which the analyzed repository was checked out. Used to relativize any absolute
     * paths in the uploaded SARIF file.
     */
    public val checkoutPath: String? = null,
    /**
     * The ref where results will be uploaded. If not provided, the Action will use the GITHUB_REF
     * environment variable. If provided, the sha input must be provided as well. This input is not
     * available in pull requests from forks.
     */
    public val ref: String? = null,
    /**
     * The sha of the HEAD of the ref where results will be uploaded. If not provided, the Action
     * will use the GITHUB_SHA environment variable. If provided, the ref input must be provided as
     * well. This input is not available in pull requests from forks.
     */
    public val sha: String? = null,
    public val token: String? = null,
    public val matrix: String? = null,
    /**
     * String used by Code Scanning for matching the analyses
     */
    public val category: String? = null,
    /**
     * If true, the Action will wait for the uploaded SARIF to be processed before completing.
     */
    public val waitForProcessing: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CodeqlActionUploadSarifV2.Outputs>("github", "codeql-action/upload-sarif",
        _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        sarifFile: String? = null,
        checkoutPath: String? = null,
        ref: String? = null,
        sha: String? = null,
        token: String? = null,
        matrix: String? = null,
        category: String? = null,
        waitForProcessing: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(sarifFile=sarifFile, checkoutPath=checkoutPath, ref=ref, sha=sha, token=token,
            matrix=matrix, category=category, waitForProcessing=waitForProcessing,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            sarifFile?.let { "sarif_file" to it },
            checkoutPath?.let { "checkout_path" to it },
            ref?.let { "ref" to it },
            sha?.let { "sha" to it },
            token?.let { "token" to it },
            matrix?.let { "matrix" to it },
            category?.let { "category" to it },
            waitForProcessing?.let { "wait-for-processing" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The ID of the uploaded SARIF file.
         */
        public val sarifId: String = "steps.$stepId.outputs.sarif-id"
    }
}
