// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.codecov

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
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
public class CodecovActionV3(
    /**
     * Repository upload token - get it from codecov.io. Required only for private repositories
     */
    public val token: String? = null,
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
     * Path to coverage file to upload
     */
    public val `file`: String? = null,
    /**
     * Comma-separated list, see the README for options and their usage
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
     * Override the assumed OS. Options are alpine | linux | macos | windows.
     */
    public val os: CodecovActionV3.OperatingSystem? = null,
    /**
     * Used when not in git/hg project to identify project root directory
     */
    public val rootDir: String? = null,
    /**
     * Specify the slug manually (Enterprise use)
     */
    public val slug: String? = null,
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("codecov", "codecov-action", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            token?.let { "token" to it },
            files?.let { "files" to it.joinToString(",") },
            directory?.let { "directory" to it },
            flags?.let { "flags" to it.joinToString(",") },
            commitParent?.let { "commit_parent" to it },
            dryRun?.let { "dry_run" to it.toString() },
            envVars?.let { "env_vars" to it.joinToString(",") },
            failCiIfError?.let { "fail_ci_if_error" to it.toString() },
            `file`?.let { "file" to it },
            functionalities?.let { "functionalities" to it.joinToString(",") },
            gcov?.let { "gcov" to it.toString() },
            gcovArgs?.let { "gcov_args" to it.joinToString(" ") },
            gcovIgnore?.let { "gcov_ignore" to it.joinToString("\n") },
            gcovInclude?.let { "gcov_include" to it.joinToString("\n") },
            moveCoverageToTrash?.let { "move_coverage_to_trash" to it.toString() },
            name?.let { "name" to it },
            overrideBranch?.let { "override_branch" to it },
            overrideBuild?.let { "override_build" to it.toString() },
            overrideCommit?.let { "override_commit" to it },
            overridePr?.let { "override_pr" to it.toString() },
            overrideTag?.let { "override_tag" to it },
            os?.let { "os" to it.stringValue },
            rootDir?.let { "root_dir" to it },
            slug?.let { "slug" to it },
            url?.let { "url" to it },
            verbose?.let { "verbose" to it.toString() },
            version?.let { "version" to it },
            workingDirectory?.let { "working-directory" to it },
            xcode?.let { "xcode" to it.toString() },
            xcodeArchivePath?.let { "xcode_archive_path" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class OperatingSystem(
        public val stringValue: String,
    ) {
        public object Alpine : CodecovActionV3.OperatingSystem("alpine")

        public object Linux : CodecovActionV3.OperatingSystem("linux")

        public object MacOs : CodecovActionV3.OperatingSystem("macos")

        public object Windows : CodecovActionV3.OperatingSystem("windows")

        public class Custom(
            customStringValue: String,
        ) : CodecovActionV3.OperatingSystem(customStringValue)
    }
}
