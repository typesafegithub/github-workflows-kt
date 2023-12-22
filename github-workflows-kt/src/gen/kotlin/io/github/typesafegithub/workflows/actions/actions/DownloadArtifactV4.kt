// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

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
 * Action: Download a Build Artifact
 *
 * Download a build artifact that was previously uploaded in the workflow by the upload-artifact
 * action
 *
 * [Action on GitHub](https://github.com/actions/download-artifact)
 */
public data class DownloadArtifactV4 private constructor(
    /**
     * Name of the artifact to download. If unspecified, all artifacts for the run are downloaded.
     */
    public val name: String? = null,
    /**
     * Destination path. Supports basic tilde expansion. Defaults to $GITHUB_WORKSPACE
     */
    public val path: String? = null,
    /**
     * A glob pattern matching the artifacts that should be downloaded. Ignored if name is
     * specified.
     */
    public val pattern: String? = null,
    /**
     * When multiple artifacts are matched, this changes the behavior of the destination
     * directories. If true, the downloaded artifacts will be in the same directory specified by path.
     * If false, the downloaded artifacts will be extracted into individual named directories within
     * the specified path.
     */
    public val mergeMultiple: Boolean? = null,
    /**
     * The GitHub token used to authenticate with the GitHub API. This is required when downloading
     * artifacts from a different repository or from a different workflow run. If this is not
     * specified, the action will attempt to download artifacts from the current repository and the
     * current workflow run.
     */
    public val githubToken: String? = null,
    /**
     * The repository owner and the repository name joined together by "/". If github-token is
     * specified, this is the repository that artifacts will be downloaded from.
     */
    public val repository: String? = null,
    /**
     * The id of the workflow run where the desired download artifact was uploaded from. If
     * github-token is specified, this is the run that artifacts will be downloaded from.
     */
    public val runId: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<DownloadArtifactV4.Outputs>("actions", "download-artifact", _customVersion ?:
        "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        path: String? = null,
        pattern: String? = null,
        mergeMultiple: Boolean? = null,
        githubToken: String? = null,
        repository: String? = null,
        runId: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(name=name, path=path, pattern=pattern, mergeMultiple=mergeMultiple,
            githubToken=githubToken, repository=repository, runId=runId,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            path?.let { "path" to it },
            pattern?.let { "pattern" to it },
            mergeMultiple?.let { "merge-multiple" to it.toString() },
            githubToken?.let { "github-token" to it },
            repository?.let { "repository" to it },
            runId?.let { "run-id" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Path of artifact download
         */
        public val downloadPath: String = "steps.$stepId.outputs.download-path"
    }
}
