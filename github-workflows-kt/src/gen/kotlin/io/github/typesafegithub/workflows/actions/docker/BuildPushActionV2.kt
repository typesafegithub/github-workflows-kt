// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.docker

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
 * Action: Build and push Docker images
 *
 * Build and push Docker images with Buildx
 *
 * [Action on GitHub](https://github.com/docker/build-push-action)
 *
 * @param addHosts List of a customs host-to-IP mapping (e.g., docker:10.180.0.1)
 * @param allow List of extra privileged entitlement (e.g., network.host,security.insecure)
 * @param buildArgs List of build-time variables
 * @param buildContexts List of additional build contexts (e.g., name=path)
 * @param builder Builder instance
 * @param cacheFrom List of external cache sources for buildx (e.g., user/app:cache,
 * type=local,src=path/to/dir)
 * @param cacheTo List of cache export destinations for buildx (e.g., user/app:cache,
 * type=local,dest=path/to/dir)
 * @param cgroupParent Optional parent cgroup for the container used in the build
 * @param context Build's context is the set of files located in the specified PATH or URL
 * @param file Path to the Dockerfile
 * @param labels List of metadata for an image
 * @param load Load is a shorthand for --output=type=docker
 * @param network Set the networking mode for the RUN instructions during build
 * @param noCache Do not use cache when building the image
 * @param outputs List of output destinations (format: type=local,dest=path)
 * @param platforms List of target platforms for build
 * @param pull Always attempt to pull all referenced images
 * @param push Push is a shorthand for --output=type=registry
 * @param secrets List of secrets to expose to the build (e.g., key=string, GIT_AUTH_TOKEN=mytoken)
 * @param secretFiles List of secret files to expose to the build (e.g., key=filename,
 * MY_SECRET=./secret.txt)
 * @param shmSize Size of /dev/shm (e.g., 2g)
 * @param ssh List of SSH agent socket or keys to expose to the build
 * @param tags List of tags
 * @param target Sets the target stage to build
 * @param ulimit Ulimit options (e.g., nofile=1024:1024)
 * @param githubToken GitHub Token used to authenticate against a repository for Git context
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: BuildPushActionV5",
    replaceWith = ReplaceWith("BuildPushActionV5"),
)
public data class BuildPushActionV2 private constructor(
    /**
     * List of a customs host-to-IP mapping (e.g., docker:10.180.0.1)
     */
    public val addHosts: List<String>? = null,
    /**
     * List of extra privileged entitlement (e.g., network.host,security.insecure)
     */
    public val allow: List<String>? = null,
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
     * List of output destinations (format: type=local,dest=path)
     */
    public val outputs: List<String>? = null,
    /**
     * List of target platforms for build
     */
    public val platforms: List<String>? = null,
    /**
     * Always attempt to pull all referenced images
     */
    public val pull: Boolean? = null,
    /**
     * Push is a shorthand for --output=type=registry
     */
    public val push: Boolean? = null,
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<BuildPushActionV2.Outputs>("docker", "build-push-action", _customVersion ?: "v2")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        addHosts: List<String>? = null,
        allow: List<String>? = null,
        buildArgs: List<String>? = null,
        buildContexts: List<String>? = null,
        builder: String? = null,
        cacheFrom: List<String>? = null,
        cacheTo: List<String>? = null,
        cgroupParent: String? = null,
        context: String? = null,
        `file`: String? = null,
        labels: List<String>? = null,
        load: Boolean? = null,
        network: String? = null,
        noCache: Boolean? = null,
        outputs: List<String>? = null,
        platforms: List<String>? = null,
        pull: Boolean? = null,
        push: Boolean? = null,
        secrets: List<String>? = null,
        secretFiles: List<String>? = null,
        shmSize: String? = null,
        ssh: List<String>? = null,
        tags: List<String>? = null,
        target: String? = null,
        ulimit: String? = null,
        githubToken: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(addHosts=addHosts, allow=allow, buildArgs=buildArgs, buildContexts=buildContexts,
            builder=builder, cacheFrom=cacheFrom, cacheTo=cacheTo, cgroupParent=cgroupParent,
            context=context, `file`=`file`, labels=labels, load=load, network=network,
            noCache=noCache, outputs=outputs, platforms=platforms, pull=pull, push=push,
            secrets=secrets, secretFiles=secretFiles, shmSize=shmSize, ssh=ssh, tags=tags,
            target=target, ulimit=ulimit, githubToken=githubToken, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            addHosts?.let { "add-hosts" to it.joinToString("\n") },
            allow?.let { "allow" to it.joinToString("\n") },
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
            outputs?.let { "outputs" to it.joinToString(",") },
            platforms?.let { "platforms" to it.joinToString(",") },
            pull?.let { "pull" to it.toString() },
            push?.let { "push" to it.toString() },
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

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
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
    }
}
