// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
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
 * Action: Create a Release
 *
 * Create a release for a tag in your repository
 *
 * [Action on GitHub](https://github.com/actions/create-release)
 */
public data class CreateReleaseV1 private constructor(
    /**
     * The name of the tag. This should come from the webhook payload, `github.GITHUB_REF` when a
     * user pushes a new tag
     */
    public val tagName: String,
    /**
     * The name of the release. For example, `Release v1.0.1`
     */
    public val releaseName: String,
    /**
     * Text describing the contents of the tag.
     */
    public val body: String? = null,
    /**
     * Path to file with information about the tag.
     */
    public val bodyPath: String? = null,
    /**
     * `true` to create a draft (unpublished) release, `false` to create a published one. Default:
     * `false`
     */
    public val draft: Boolean? = null,
    /**
     * `true` to identify the release as a prerelease. `false` to identify the release as a full
     * release. Default: `false`
     */
    public val prerelease: Boolean? = null,
    /**
     * Any branch or commit SHA the Git tag is created from, unused if the Git tag already exists.
     * Default: SHA of current commit
     */
    public val commitish: String? = null,
    /**
     * Owner of the repository if it is not the current one
     */
    public val owner: String? = null,
    /**
     * Repository on which to release.  Used only if you want to create the release on another repo
     */
    public val repo: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CreateReleaseV1.Outputs>("actions", "create-release", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        tagName: String,
        releaseName: String,
        body: String? = null,
        bodyPath: String? = null,
        draft: Boolean? = null,
        prerelease: Boolean? = null,
        commitish: String? = null,
        owner: String? = null,
        repo: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(tagName=tagName, releaseName=releaseName, body=body, bodyPath=bodyPath, draft=draft,
            prerelease=prerelease, commitish=commitish, owner=owner, repo=repo,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "tag_name" to tagName,
            "release_name" to releaseName,
            body?.let { "body" to it },
            bodyPath?.let { "body_path" to it },
            draft?.let { "draft" to it.toString() },
            prerelease?.let { "prerelease" to it.toString() },
            commitish?.let { "commitish" to it },
            owner?.let { "owner" to it },
            repo?.let { "repo" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The ID of the created Release
         */
        public val id: String = "steps.$stepId.outputs.id"

        /**
         * The URL users can navigate to in order to view the release
         */
        public val htmlUrl: String = "steps.$stepId.outputs.html_url"

        /**
         * The URL for uploading assets to the release
         */
        public val uploadUrl: String = "steps.$stepId.outputs.upload_url"
    }
}
