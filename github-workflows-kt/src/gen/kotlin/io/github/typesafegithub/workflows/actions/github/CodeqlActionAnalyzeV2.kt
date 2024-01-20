// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.github

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
 * Action: CodeQL: Finish
 *
 * Finalize CodeQL database
 *
 * [Action on GitHub](https://github.com/github/codeql-action/tree/v2/analyze)
 *
 * @param checkName The name of the check run to add text to.
 * @param output The path of the directory in which to save the SARIF results
 * @param upload Upload the SARIF file to Code Scanning. Defaults to 'always' which uploads the
 * SARIF file to Code Scanning for successful and failed runs. 'failure-only' only uploads debugging
 * information to Code Scanning if the workflow run fails, for users post-processing the SARIF file
 * before uploading it to Code Scanning. 'never' avoids uploading the SARIF file to Code Scanning, even
 * if the code scanning run fails. This is not recommended for external users since it complicates
 * debugging.
 * @param cleanupLevel Level of cleanup to perform on CodeQL databases at the end of the analyze
 * step. This should either be 'none' to skip cleanup, or be a valid argument for the --mode flag of
 * the CodeQL CLI command 'codeql database cleanup' as documented at
 * https://codeql.github.com/docs/codeql-cli/manual/database-cleanup
 * @param ram The amount of memory in MB that can be used by CodeQL for database finalization and
 * query execution. By default, this action will use the same amount of memory as previously set in the
 * "init" action. If the "init" action also does not have an explicit "ram" input, this action will use
 * most of the memory available in the system (which for GitHub-hosted runners is 6GB for Linux, 5.5GB
 * for Windows, and 13GB for macOS).
 * @param addSnippets Specify whether or not to add code snippets to the output sarif file.
 * @param skipQueries If this option is set, the CodeQL database will be built but no queries will
 * be run on it. Thus, no results will be produced.
 * @param threads The number of threads that can be used by CodeQL for database finalization and
 * query execution. By default, this action will use the same number of threads as previously set in
 * the "init" action. If the "init" action also does not have an explicit "threads" input, this action
 * will use all the hardware threads available in the system (which for GitHub-hosted runners is 2 for
 * Linux and Windows and 3 for macOS).
 * @param checkoutPath The path at which the analyzed repository was checked out. Used to relativize
 * any absolute paths in the uploaded SARIF file.
 * @param ref The ref where results will be uploaded. If not provided, the Action will use the
 * GITHUB_REF environment variable. If provided, the sha input must be provided as well. This input is
 * not available in pull requests from forks.
 * @param sha The sha of the HEAD of the ref where results will be uploaded. If not provided, the
 * Action will use the GITHUB_SHA environment variable. If provided, the ref input must be provided as
 * well. This input is not available in pull requests from forks.
 * @param category String used by Code Scanning for matching the analyses
 * @param uploadDatabase Whether to upload the resulting CodeQL database
 * @param waitForProcessing If true, the Action will wait for the uploaded SARIF to be processed
 * before completing.
 * @param expectError [Internal] It is an error to use this input outside of integration testing of
 * the codeql-action.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class CodeqlActionAnalyzeV2 private constructor(
    /**
     * The name of the check run to add text to.
     */
    public val checkName: String? = null,
    /**
     * The path of the directory in which to save the SARIF results
     */
    public val output: String? = null,
    /**
     * Upload the SARIF file to Code Scanning. Defaults to 'always' which uploads the SARIF file to
     * Code Scanning for successful and failed runs. 'failure-only' only uploads debugging information
     * to Code Scanning if the workflow run fails, for users post-processing the SARIF file before
     * uploading it to Code Scanning. 'never' avoids uploading the SARIF file to Code Scanning, even if
     * the code scanning run fails. This is not recommended for external users since it complicates
     * debugging.
     */
    public val upload: CodeqlActionAnalyzeV2.Upload? = null,
    /**
     * Level of cleanup to perform on CodeQL databases at the end of the analyze step. This should
     * either be 'none' to skip cleanup, or be a valid argument for the --mode flag of the CodeQL CLI
     * command 'codeql database cleanup' as documented at
     * https://codeql.github.com/docs/codeql-cli/manual/database-cleanup
     */
    public val cleanupLevel: CodeqlActionAnalyzeV2.CleanupLevel? = null,
    /**
     * The amount of memory in MB that can be used by CodeQL for database finalization and query
     * execution. By default, this action will use the same amount of memory as previously set in the
     * "init" action. If the "init" action also does not have an explicit "ram" input, this action will
     * use most of the memory available in the system (which for GitHub-hosted runners is 6GB for
     * Linux, 5.5GB for Windows, and 13GB for macOS).
     */
    public val ram: Int? = null,
    /**
     * Specify whether or not to add code snippets to the output sarif file.
     */
    public val addSnippets: Boolean? = null,
    /**
     * If this option is set, the CodeQL database will be built but no queries will be run on it.
     * Thus, no results will be produced.
     */
    public val skipQueries: Boolean? = null,
    /**
     * The number of threads that can be used by CodeQL for database finalization and query
     * execution. By default, this action will use the same number of threads as previously set in the
     * "init" action. If the "init" action also does not have an explicit "threads" input, this action
     * will use all the hardware threads available in the system (which for GitHub-hosted runners is 2
     * for Linux and Windows and 3 for macOS).
     */
    public val threads: Int? = null,
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
    /**
     * String used by Code Scanning for matching the analyses
     */
    public val category: String? = null,
    /**
     * Whether to upload the resulting CodeQL database
     */
    public val uploadDatabase: Boolean? = null,
    /**
     * If true, the Action will wait for the uploaded SARIF to be processed before completing.
     */
    public val waitForProcessing: Boolean? = null,
    public val token: String? = null,
    public val matrix: String? = null,
    /**
     * [Internal] It is an error to use this input outside of integration testing of the
     * codeql-action.
     */
    public val expectError: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CodeqlActionAnalyzeV2.Outputs>("github", "codeql-action/analyze", _customVersion
        ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        checkName: String? = null,
        output: String? = null,
        upload: CodeqlActionAnalyzeV2.Upload? = null,
        cleanupLevel: CodeqlActionAnalyzeV2.CleanupLevel? = null,
        ram: Int? = null,
        addSnippets: Boolean? = null,
        skipQueries: Boolean? = null,
        threads: Int? = null,
        checkoutPath: String? = null,
        ref: String? = null,
        sha: String? = null,
        category: String? = null,
        uploadDatabase: Boolean? = null,
        waitForProcessing: Boolean? = null,
        token: String? = null,
        matrix: String? = null,
        expectError: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(checkName=checkName, output=output, upload=upload, cleanupLevel=cleanupLevel, ram=ram,
            addSnippets=addSnippets, skipQueries=skipQueries, threads=threads,
            checkoutPath=checkoutPath, ref=ref, sha=sha, category=category,
            uploadDatabase=uploadDatabase, waitForProcessing=waitForProcessing, token=token,
            matrix=matrix, expectError=expectError, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            checkName?.let { "check_name" to it },
            output?.let { "output" to it },
            upload?.let { "upload" to it.stringValue },
            cleanupLevel?.let { "cleanup-level" to it.stringValue },
            ram?.let { "ram" to it.toString() },
            addSnippets?.let { "add-snippets" to it.toString() },
            skipQueries?.let { "skip-queries" to it.toString() },
            threads?.let { "threads" to it.toString() },
            checkoutPath?.let { "checkout_path" to it },
            ref?.let { "ref" to it },
            sha?.let { "sha" to it },
            category?.let { "category" to it },
            uploadDatabase?.let { "upload-database" to it.toString() },
            waitForProcessing?.let { "wait-for-processing" to it.toString() },
            token?.let { "token" to it },
            matrix?.let { "matrix" to it },
            expectError?.let { "expect-error" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Upload(
        public val stringValue: String,
    ) {
        public object Always : CodeqlActionAnalyzeV2.Upload("always")

        public object FailureOnly : CodeqlActionAnalyzeV2.Upload("failure-only")

        public object Never : CodeqlActionAnalyzeV2.Upload("never")

        public class Custom(
            customStringValue: String,
        ) : CodeqlActionAnalyzeV2.Upload(customStringValue)
    }

    public sealed class CleanupLevel(
        public val stringValue: String,
    ) {
        public object None : CodeqlActionAnalyzeV2.CleanupLevel("none")

        public object Brutal : CodeqlActionAnalyzeV2.CleanupLevel("brutal")

        public object Normal : CodeqlActionAnalyzeV2.CleanupLevel("normal")

        public object Light : CodeqlActionAnalyzeV2.CleanupLevel("light")

        public class Custom(
            customStringValue: String,
        ) : CodeqlActionAnalyzeV2.CleanupLevel(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A map from language to absolute path for each database created by CodeQL.
         */
        public val dbLocations: String = "steps.$stepId.outputs.db-locations"

        /**
         * The ID of the uploaded SARIF file.
         */
        public val sarifId: String = "steps.$stepId.outputs.sarif-id"
    }
}
