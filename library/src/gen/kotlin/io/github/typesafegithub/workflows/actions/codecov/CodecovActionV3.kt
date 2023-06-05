// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.codecov

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Codecov
 *
 * GitHub Action that uploads coverage reports for your repository to codecov.io
 *
 * [Action on GitHub](https://github.com/codecov/codecov-action)
 */
public data class CodecovActionV3 private constructor(
    /**
     * Repository upload token - get it from codecov.io. Required only for private repositories
     */
    public val token: String? = null,
    /**
     * Path to coverage file to upload
     */
    public val `file`: String? = null,
    /**
     * Comma-separated list of files to upload
     */
    public val files: List<String>? = null,
    /**
     * Directory to search for coverage reports.
     */
    public val directory: String? = null,
    /**
     * Flag upload to group coverage metrics (e.g. unittests | integration | ui,chrome)
     */
    public val flags: List<String>? = null,
    /**
     * Specify the path of a full Codecov report to re-upload
     */
    public val fullReport: String? = null,
    /**
     * The commit SHA of the parent for which you are uploading coverage. If not present, the parent
     * will be determined using the API of your repository provider.  When using the repository
     * providers API, the parent is determined via finding the closest ancestor to the commit.
     */
    public val commitParent: String? = null,
    /**
     * Don't upload files to Codecov
     */
    public val dryRun: Boolean? = null,
    /**
     * Environment variables to tag the upload with (e.g. PYTHON | OS,PYTHON)
     */
    public val envVars: List<String>? = null,
    /**
     * Specify whether or not CI build should fail if Codecov runs into an error during upload
     */
    public val failCiIfError: Boolean? = null,
    /**
     * Comma-separated list, see the README for options and their usage. Options include `network`,
     * `fixes`, `search`.
     */
    public val functionalities: List<String>? = null,
    /**
     * Run with gcov support
     */
    public val gcov: Boolean? = null,
    /**
     * Extra arguments to pass to gcov
     */
    public val gcovArgs: List<String>? = null,
    /**
     * gcov executable to run. Defaults to gcov
     */
    public val gcovExecutable: String? = null,
    /**
     * Paths to ignore during gcov gathering
     */
    public val gcovIgnore: List<String>? = null,
    /**
     * Paths to include during gcov gathering
     */
    public val gcovInclude: List<String>? = null,
    /**
     * Move discovered coverage reports to the trash
     */
    public val moveCoverageToTrash: Boolean? = null,
    /**
     * User defined upload name. Visible in Codecov UI
     */
    public val name: String? = null,
    /**
     * Specify a filter on the files listed in the network section of the Codecov report. Useful for
     * upload-specific path fixing
     */
    public val networkFilter: String? = null,
    /**
     * Specify a prefix on files listed in the network section of the Codecov report. Useful to help
     * resolve path fixing
     */
    public val networkPrefix: String? = null,
    /**
     * Override the assumed OS. Options are aarch64 | alpine | linux | macos | windows.
     */
    public val os: CodecovActionV3.OperatingSystem? = null,
    /**
     * Specify the branch name
     */
    public val overrideBranch: String? = null,
    /**
     * Specify the build number
     */
    public val overrideBuild: Int? = null,
    /**
     * Specify the commit SHA
     */
    public val overrideCommit: String? = null,
    /**
     * Specify the pull request number
     */
    public val overridePr: Int? = null,
    /**
     * Specify the git tag
     */
    public val overrideTag: String? = null,
    /**
     * Used when not in git/hg project to identify project root directory
     */
    public val rootDir: String? = null,
    /**
     * Specify the slug manually (Enterprise use)
     */
    public val slug: String? = null,
    /**
     * Run with swift coverage support
     */
    public val swift: Boolean? = null,
    /**
     * Specify the swift project to speed up coverage conversion
     */
    public val swiftProject: String? = null,
    /**
     * The upstream http proxy server to connect through
     */
    public val upstreamProxy: String? = null,
    /**
     * Change the upload host (Enterprise use)
     */
    public val url: String? = null,
    /**
     * Specify whether the Codecov output should be verbose
     */
    public val verbose: Boolean? = null,
    /**
     * Specify which version of the Codecov Uploader should be used. Defaults to `latest`
     */
    public val version: String? = null,
    /**
     * Directory in which to execute codecov.sh
     */
    public val workingDirectory: String? = null,
    /**
     * Run with xcode support
     */
    public val xcode: Boolean? = null,
    /**
     * Specify the xcode archive path. Likely specified as the -resultBundlePath and should end in
     * .xcresult
     */
    public val xcodeArchivePath: String? = null,
    /**
     * Add additional uploader args that may be missing in the Action
     */
    public val xtraArgs: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("codecov", "codecov-action", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        token: String? = null,
        `file`: String? = null,
        files: List<String>? = null,
        directory: String? = null,
        flags: List<String>? = null,
        fullReport: String? = null,
        commitParent: String? = null,
        dryRun: Boolean? = null,
        envVars: List<String>? = null,
        failCiIfError: Boolean? = null,
        functionalities: List<String>? = null,
        gcov: Boolean? = null,
        gcovArgs: List<String>? = null,
        gcovExecutable: String? = null,
        gcovIgnore: List<String>? = null,
        gcovInclude: List<String>? = null,
        moveCoverageToTrash: Boolean? = null,
        name: String? = null,
        networkFilter: String? = null,
        networkPrefix: String? = null,
        os: CodecovActionV3.OperatingSystem? = null,
        overrideBranch: String? = null,
        overrideBuild: Int? = null,
        overrideCommit: String? = null,
        overridePr: Int? = null,
        overrideTag: String? = null,
        rootDir: String? = null,
        slug: String? = null,
        swift: Boolean? = null,
        swiftProject: String? = null,
        upstreamProxy: String? = null,
        url: String? = null,
        verbose: Boolean? = null,
        version: String? = null,
        workingDirectory: String? = null,
        xcode: Boolean? = null,
        xcodeArchivePath: String? = null,
        xtraArgs: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(token=token, `file`=`file`, files=files, directory=directory, flags=flags,
            fullReport=fullReport, commitParent=commitParent, dryRun=dryRun, envVars=envVars,
            failCiIfError=failCiIfError, functionalities=functionalities, gcov=gcov,
            gcovArgs=gcovArgs, gcovExecutable=gcovExecutable, gcovIgnore=gcovIgnore,
            gcovInclude=gcovInclude, moveCoverageToTrash=moveCoverageToTrash, name=name,
            networkFilter=networkFilter, networkPrefix=networkPrefix, os=os,
            overrideBranch=overrideBranch, overrideBuild=overrideBuild,
            overrideCommit=overrideCommit, overridePr=overridePr, overrideTag=overrideTag,
            rootDir=rootDir, slug=slug, swift=swift, swiftProject=swiftProject,
            upstreamProxy=upstreamProxy, url=url, verbose=verbose, version=version,
            workingDirectory=workingDirectory, xcode=xcode, xcodeArchivePath=xcodeArchivePath,
            xtraArgs=xtraArgs, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            token?.let { "token" to it },
            `file`?.let { "file" to it },
            files?.let { "files" to it.joinToString(",") },
            directory?.let { "directory" to it },
            flags?.let { "flags" to it.joinToString(",") },
            fullReport?.let { "full_report" to it },
            commitParent?.let { "commit_parent" to it },
            dryRun?.let { "dry_run" to it.toString() },
            envVars?.let { "env_vars" to it.joinToString(",") },
            failCiIfError?.let { "fail_ci_if_error" to it.toString() },
            functionalities?.let { "functionalities" to it.joinToString(",") },
            gcov?.let { "gcov" to it.toString() },
            gcovArgs?.let { "gcov_args" to it.joinToString(" ") },
            gcovExecutable?.let { "gcov_executable" to it },
            gcovIgnore?.let { "gcov_ignore" to it.joinToString("\n") },
            gcovInclude?.let { "gcov_include" to it.joinToString("\n") },
            moveCoverageToTrash?.let { "move_coverage_to_trash" to it.toString() },
            name?.let { "name" to it },
            networkFilter?.let { "network_filter" to it },
            networkPrefix?.let { "network_prefix" to it },
            os?.let { "os" to it.stringValue },
            overrideBranch?.let { "override_branch" to it },
            overrideBuild?.let { "override_build" to it.toString() },
            overrideCommit?.let { "override_commit" to it },
            overridePr?.let { "override_pr" to it.toString() },
            overrideTag?.let { "override_tag" to it },
            rootDir?.let { "root_dir" to it },
            slug?.let { "slug" to it },
            swift?.let { "swift" to it.toString() },
            swiftProject?.let { "swift_project" to it },
            upstreamProxy?.let { "upstream_proxy" to it },
            url?.let { "url" to it },
            verbose?.let { "verbose" to it.toString() },
            version?.let { "version" to it },
            workingDirectory?.let { "working-directory" to it },
            xcode?.let { "xcode" to it.toString() },
            xcodeArchivePath?.let { "xcode_archive_path" to it },
            xtraArgs?.let { "xtra_args" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class OperatingSystem(
        public val stringValue: String,
    ) {
        public object Aarch64 : CodecovActionV3.OperatingSystem("aarch64")

        public object Alpine : CodecovActionV3.OperatingSystem("alpine")

        public object Linux : CodecovActionV3.OperatingSystem("linux")

        public object Macos : CodecovActionV3.OperatingSystem("macos")

        public object Windows : CodecovActionV3.OperatingSystem("windows")

        public class Custom(
            customStringValue: String,
        ) : CodecovActionV3.OperatingSystem(customStringValue)
    }
}
