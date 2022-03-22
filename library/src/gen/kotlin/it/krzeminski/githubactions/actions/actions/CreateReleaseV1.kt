// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
public class CreateReleaseV1(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : ActionWithOutputs<CreateReleaseV1.Outputs>("actions", "create-release", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
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

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
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

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
