// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.elgohr

import it.krzeminski.githubactions.domain.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Publish Docker
 *
 * Uses the git branch as the docker tag and pushes the container
 *
 * [Action on GitHub](https://github.com/elgohr/Publish-Docker-Github-Action)
 */
@Deprecated(
    message = "This action has a newer major version: PublishDockerGithubActionV5",
    replaceWith = ReplaceWith("PublishDockerGithubActionV5"),
)
public data class PublishDockerGithubActionV4(
    /**
     * The name of the image you would like to push
     */
    public val name: String,
    /**
     * The login username for the registry
     */
    public val username: String,
    /**
     * The login password for the registry
     */
    public val password: String,
    /**
     * Use registry for pushing to a custom registry
     */
    public val registry: String? = null,
    /**
     * Use snapshot to push an additional image
     */
    public val snapshot: Boolean? = null,
    /**
     * Set the default branch of your repository (default: master)
     */
    public val defaultBranch: String? = null,
    /**
     * Use dockerfile when you would like to explicitly build a Dockerfile
     */
    public val dockerfile: String? = null,
    /**
     * Use workdir when you would like to change the directory for building
     */
    public val workdir: String? = null,
    /**
     * Use context when you would like to change the Docker build context.
     */
    public val context: String? = null,
    /**
     * Use buildargs when you want to pass a list of environment variables as build-args
     */
    public val buildargs: List<String>? = null,
    /**
     * Use buildoptions when you want to configure options for building
     */
    public val buildoptions: String? = null,
    /**
     * Use cache when you have big images, that you would only like to build partially
     */
    public val cache: Boolean? = null,
    /**
     * Use tags when you want to bring your own tags (separated by comma)
     */
    public val tags: List<String>? = null,
    /**
     * Use tag_names when you want to push tags/release by their git name
     */
    public val tagNames: Boolean? = null,
    /**
     * Push semver docker tags. e.g. image:1.2.3, image:1.2, image:1
     */
    public val tagSemver: Boolean? = null,
    /**
     * Set no_push to true if you want to prevent the action from pushing to a registry (default:
     * false)
     */
    public val noPush: Boolean? = null,
    /**
     * Use platforms for building multi-arch images
     */
    public val platforms: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<PublishDockerGithubActionV4.Outputs>("elgohr", "Publish-Docker-Github-Action",
        _customVersion ?: "v4") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "name" to name,
            "username" to username,
            "password" to password,
            registry?.let { "registry" to it },
            snapshot?.let { "snapshot" to it.toString() },
            defaultBranch?.let { "default_branch" to it },
            dockerfile?.let { "dockerfile" to it },
            workdir?.let { "workdir" to it },
            context?.let { "context" to it },
            buildargs?.let { "buildargs" to it.joinToString(",") },
            buildoptions?.let { "buildoptions" to it },
            cache?.let { "cache" to it.toString() },
            tags?.let { "tags" to it.joinToString(",") },
            tagNames?.let { "tag_names" to it.toString() },
            tagSemver?.let { "tag_semver" to it.toString() },
            noPush?.let { "no_push" to it.toString() },
            platforms?.let { "platforms" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Is the tag, which was pushed
         */
        public val tag: String = "steps.$stepId.outputs.tag"

        /**
         * Is the tag that is generated by the snapshot-option and pushed
         */
        public val snapshotTag: String = "steps.$stepId.outputs.snapshot-tag"

        /**
         * Is the digest of the image, which was pushed
         */
        public val digest: String = "steps.$stepId.outputs.digest"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
