// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.docker

import it.krzeminski.githubactions.actions.ActionWithOutputs
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
 * Action: Build and push Docker images
 *
 * Build and push Docker images with Buildx
 *
 * [Action on GitHub](https://github.com/docker/build-push-action)
 */
@Deprecated(
    message = "This action has a newer major version: BuildPushActionV4",
    replaceWith = ReplaceWith("BuildPushActionV4"),
)
public data class BuildPushActionV3(
    /**
     * List of a customs host-to-IP mapping (e.g., docker:10.180.0.1)
     */
    public val addHosts: List<String>? = null,
    /**
     * List of extra privileged entitlement (e.g., network.host,security.insecure)
     */
    public val allow: List<String>? = null,
    /**
     * List of attestation parameters (e.g., type=sbom,generator=image)
     */
    public val attests: List<String>? = null,
    /**
     * List of build-time variables
     */
    public val buildArgs: List<String>? = null,
    /**
     * List of additional build contexts (e.g., name=path)
     */
    public val buildContexts: List<String>? = null,
    /**
     * Builder instance
     */
    public val builder: String? = null,
    /**
     * List of external cache sources for buildx (e.g., user/app:cache, type=local,src=path/to/dir)
     */
    public val cacheFrom: List<String>? = null,
    /**
     * List of cache export destinations for buildx (e.g., user/app:cache,
     * type=local,dest=path/to/dir)
     */
    public val cacheTo: List<String>? = null,
    /**
     * Optional parent cgroup for the container used in the build
     */
    public val cgroupParent: String? = null,
    /**
     * Build's context is the set of files located in the specified PATH or URL
     */
    public val context: String? = null,
    /**
     * Path to the Dockerfile
     */
    public val `file`: String? = null,
    /**
     * List of metadata for an image
     */
    public val labels: List<String>? = null,
    /**
     * Load is a shorthand for --output=type=docker
     */
    public val load: Boolean? = null,
    /**
     * Set the networking mode for the RUN instructions during build
     */
    public val network: String? = null,
    /**
     * Do not use cache when building the image
     */
    public val noCache: Boolean? = null,
    /**
     * Do not cache specified stages
     */
    public val noCacheFilters: List<String>? = null,
    /**
     * List of output destinations (format: type=local,dest=path)
     */
    public val outputs: List<String>? = null,
    /**
     * List of target platforms for build
     */
    public val platforms: List<String>? = null,
    /**
     * Generate provenance attestation for the build (shorthand for --attest=type=provenance)
     */
    public val provenance: Boolean? = null,
    /**
     * Always attempt to pull all referenced images
     */
    public val pull: Boolean? = null,
    /**
     * Push is a shorthand for --output=type=registry
     */
    public val push: Boolean? = null,
    /**
     * Generate SBOM attestation for the build (shorthand for --attest=type=sbom)
     */
    public val sbom: Boolean? = null,
    /**
     * List of secrets to expose to the build (e.g., key=string, GIT_AUTH_TOKEN=mytoken)
     */
    public val secrets: List<String>? = null,
    /**
     * List of secret files to expose to the build (e.g., key=filename, MY_SECRET=./secret.txt)
     */
    public val secretFiles: List<String>? = null,
    /**
     * Size of /dev/shm (e.g., 2g)
     */
    public val shmSize: String? = null,
    /**
     * List of SSH agent socket or keys to expose to the build
     */
    public val ssh: List<String>? = null,
    /**
     * List of tags
     */
    public val tags: List<String>? = null,
    /**
     * Sets the target stage to build
     */
    public val target: String? = null,
    /**
     * Ulimit options (e.g., nofile=1024:1024)
     */
    public val ulimit: String? = null,
    /**
     * GitHub Token used to authenticate against a repository for Git context
     */
    public val githubToken: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<BuildPushActionV3.Outputs>("docker", "build-push-action", _customVersion ?:
        "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            addHosts?.let { "add-hosts" to it.joinToString("\n") },
            allow?.let { "allow" to it.joinToString("\n") },
            attests?.let { "attests" to it.joinToString(",") },
            buildArgs?.let { "build-args" to it.joinToString("\n") },
            buildContexts?.let { "build-contexts" to it.joinToString("\n") },
            builder?.let { "builder" to it },
            cacheFrom?.let { "cache-from" to it.joinToString("\n") },
            cacheTo?.let { "cache-to" to it.joinToString("\n") },
            cgroupParent?.let { "cgroup-parent" to it },
            context?.let { "context" to it },
            `file`?.let { "file" to it },
            labels?.let { "labels" to it.joinToString(",") },
            load?.let { "load" to it.toString() },
            network?.let { "network" to it },
            noCache?.let { "no-cache" to it.toString() },
            noCacheFilters?.let { "no-cache-filters" to it.joinToString(",") },
            outputs?.let { "outputs" to it.joinToString(",") },
            platforms?.let { "platforms" to it.joinToString(",") },
            provenance?.let { "provenance" to it.toString() },
            pull?.let { "pull" to it.toString() },
            push?.let { "push" to it.toString() },
            sbom?.let { "sbom" to it.toString() },
            secrets?.let { "secrets" to it.joinToString("\n") },
            secretFiles?.let { "secret-files" to it.joinToString("\n") },
            shmSize?.let { "shm-size" to it },
            ssh?.let { "ssh" to it.joinToString("\n") },
            tags?.let { "tags" to it.joinToString("\n") },
            target?.let { "target" to it },
            ulimit?.let { "ulimit" to it },
            githubToken?.let { "github-token" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Image ID
         */
        public val imageid: String = "steps.$stepId.outputs.imageid"

        /**
         * Image digest
         */
        public val digest: String = "steps.$stepId.outputs.digest"

        /**
         * Build result metadata
         */
        public val metadata: String = "steps.$stepId.outputs.metadata"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
