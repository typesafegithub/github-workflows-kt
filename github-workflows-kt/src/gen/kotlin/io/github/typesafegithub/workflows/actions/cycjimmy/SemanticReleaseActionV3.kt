// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.cycjimmy

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Action For Semantic Release
 *
 * GitHub Action for Semantic Release
 *
 * [Action on GitHub](https://github.com/cycjimmy/semantic-release-action)
 *
 * @param semanticVersion Specify specifying version range for semantic-release. If no version range
 * is specified, latest version will be used by default
 * @param branches The branches on which releases should happen. It will override the branches
 * attribute in your configuration file. Support for semantic-release above v16. See
 * https://semantic-release.gitbook.io/semantic-release/usage/configuration#branches for more
 * information.
 * @param branch The branch on which releases should happen. It will override the branch attribute
 * in your configuration file. If the attribute is not configured on both sides, the default is master.
 * Support for semantic-release older than v16.
 * @param extraPlugins Extra plugins for pre-install. You can also specify specifying version range
 * for the extra plugins if you prefer.
 * @param dryRun Whether to run semantic release in `dry-run` mode. It will override the dryRun
 * attribute in your configuration file
 * @param ci Whether to run semantic release with CI support (default: true). It will override the
 * ci attribute in your configuration file
 * @param extends One or several sharable configurations,
 * https://semantic-release.gitbook.io/semantic-release/usage/configuration#extends
 * @param workingDirectory Specify another working directory for semantic release. Default one is
 * provided by github.
 * @param tagFormat The default tag format on semantic-release is v{version}. You can override that
 * behavior using this option.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: SemanticReleaseActionV4",
    replaceWith = ReplaceWith("SemanticReleaseActionV4"),
)
public data class SemanticReleaseActionV3 private constructor(
    /**
     * Specify specifying version range for semantic-release. If no version range is specified,
     * latest version will be used by default
     */
    public val semanticVersion: String? = null,
    /**
     * The branches on which releases should happen. It will override the branches attribute in your
     * configuration file. Support for semantic-release above v16. See
     * https://semantic-release.gitbook.io/semantic-release/usage/configuration#branches for more
     * information.
     */
    public val branches: String? = null,
    /**
     * The branch on which releases should happen. It will override the branch attribute in your
     * configuration file. If the attribute is not configured on both sides, the default is master.
     * Support for semantic-release older than v16.
     */
    public val branch: String? = null,
    /**
     * Extra plugins for pre-install. You can also specify specifying version range for the extra
     * plugins if you prefer.
     */
    public val extraPlugins: List<String>? = null,
    /**
     * Whether to run semantic release in `dry-run` mode. It will override the dryRun attribute in
     * your configuration file
     */
    public val dryRun: Boolean? = null,
    /**
     * Whether to run semantic release with CI support (default: true). It will override the ci
     * attribute in your configuration file
     */
    public val ci: Boolean? = null,
    /**
     * One or several sharable configurations,
     * https://semantic-release.gitbook.io/semantic-release/usage/configuration#extends
     */
    public val extends: List<String>? = null,
    /**
     * Specify another working directory for semantic release. Default one is provided by github.
     */
    public val workingDirectory: String? = null,
    /**
     * The default tag format on semantic-release is v{version}. You can override that behavior
     * using this option.
     */
    public val tagFormat: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SemanticReleaseActionV3.Outputs>("cycjimmy", "semantic-release-action",
        _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        semanticVersion: String? = null,
        branches: String? = null,
        branch: String? = null,
        extraPlugins: List<String>? = null,
        dryRun: Boolean? = null,
        ci: Boolean? = null,
        extends: List<String>? = null,
        workingDirectory: String? = null,
        tagFormat: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(semanticVersion=semanticVersion, branches=branches, branch=branch,
            extraPlugins=extraPlugins, dryRun=dryRun, ci=ci, extends=extends,
            workingDirectory=workingDirectory, tagFormat=tagFormat, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            semanticVersion?.let { "semantic_version" to it },
            branches?.let { "branches" to it },
            branch?.let { "branch" to it },
            extraPlugins?.let { "extra_plugins" to it.joinToString("\n") },
            dryRun?.let { "dry_run" to it.toString() },
            ci?.let { "ci" to it.toString() },
            extends?.let { "extends" to it.joinToString("\n") },
            workingDirectory?.let { "working_directory" to it },
            tagFormat?.let { "tag_format" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Whether a new release was published
         */
        public val newReleasePublished: String = "steps.$stepId.outputs.new_release_published"

        /**
         * Version of the new release
         */
        public val newReleaseVersion: String = "steps.$stepId.outputs.new_release_version"

        /**
         * Major version of the new release
         */
        public val newReleaseMajorVersion: String =
                "steps.$stepId.outputs.new_release_major_version"

        /**
         * Minor version of the new release
         */
        public val newReleaseMinorVersion: String =
                "steps.$stepId.outputs.new_release_minor_version"

        /**
         * Patch version of the new release
         */
        public val newReleasePatchVersion: String =
                "steps.$stepId.outputs.new_release_patch_version"

        /**
         * The distribution channel on which the last release was initially made available
         * (undefined for the default distribution channel).
         */
        public val newReleaseChannel: String = "steps.$stepId.outputs.new_release_channel"

        /**
         * The release notes for the new release.
         */
        public val newReleaseNotes: String = "steps.$stepId.outputs.new_release_notes"

        /**
         * The sha of the last commit being part of the new release.
         */
        public val newReleaseGitHead: String = "steps.$stepId.outputs.new_release_git_head"

        /**
         * The Git tag associated with the new release.
         */
        public val newReleaseGitTag: String = "steps.$stepId.outputs.new_release_git_tag"

        /**
         * Version of the previous release, if there was one.
         */
        public val lastReleaseVersion: String = "steps.$stepId.outputs.last_release_version"

        /**
         * The sha of the last commit being part of the last release, if there was one.
         */
        public val lastReleaseGitHead: String = "steps.$stepId.outputs.last_release_git_head"

        /**
         * The Git tag associated with the last release, if there was one.
         */
        public val lastReleaseGitTag: String = "steps.$stepId.outputs.last_release_git_tag"
    }
}
