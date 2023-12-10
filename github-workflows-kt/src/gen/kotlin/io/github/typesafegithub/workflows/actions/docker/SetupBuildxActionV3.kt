// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.docker

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Docker Setup Buildx
 *
 * Set up Docker Buildx
 *
 * [Action on GitHub](https://github.com/docker/setup-buildx-action)
 */
public data class SetupBuildxActionV3 private constructor(
    /**
     * Buildx version. (eg. v0.3.0)
     */
    public val version: String? = null,
    /**
     * Sets the builder driver to be used
     */
    public val driver: String? = null,
    /**
     * List of additional driver-specific options. (eg. image=moby/buildkit:master)
     */
    public val driverOpts: List<String>? = null,
    /**
     * Flags for buildkitd daemon
     */
    public val buildkitdFlags: String? = null,
    /**
     * Sets up docker build command as an alias to docker buildx build
     */
    public val install: Boolean? = null,
    /**
     * Switch to this builder instance
     */
    public val use: Boolean? = null,
    /**
     * Optional address for docker socket or context from `docker context ls`
     */
    public val endpoint: String? = null,
    /**
     * Fixed platforms for current node. If not empty, values take priority over the detected ones
     */
    public val platforms: List<String>? = null,
    /**
     * BuildKit config file
     */
    public val config: String? = null,
    /**
     * Inline BuildKit config
     */
    public val configInline: String? = null,
    /**
     * Append additional nodes to the builder
     */
    public val append: String? = null,
    /**
     * Cleanup temp files and remove builder at the end of a job
     */
    public val cleanup: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupBuildxActionV3.Outputs>("docker", "setup-buildx-action", _customVersion ?:
        "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        version: String? = null,
        driver: String? = null,
        driverOpts: List<String>? = null,
        buildkitdFlags: String? = null,
        install: Boolean? = null,
        use: Boolean? = null,
        endpoint: String? = null,
        platforms: List<String>? = null,
        config: String? = null,
        configInline: String? = null,
        append: String? = null,
        cleanup: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(version=version, driver=driver, driverOpts=driverOpts, buildkitdFlags=buildkitdFlags,
            install=install, use=use, endpoint=endpoint, platforms=platforms, config=config,
            configInline=configInline, append=append, cleanup=cleanup, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            version?.let { "version" to it },
            driver?.let { "driver" to it },
            driverOpts?.let { "driver-opts" to it.joinToString("\n") },
            buildkitdFlags?.let { "buildkitd-flags" to it },
            install?.let { "install" to it.toString() },
            use?.let { "use" to it.toString() },
            endpoint?.let { "endpoint" to it },
            platforms?.let { "platforms" to it.joinToString(",") },
            config?.let { "config" to it },
            configInline?.let { "config-inline" to it },
            append?.let { "append" to it },
            cleanup?.let { "cleanup" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Builder name
         */
        public val name: String = "steps.$stepId.outputs.name"

        /**
         * Builder driver
         */
        public val driver: String = "steps.$stepId.outputs.driver"

        /**
         * Builder node platforms (preferred or available)
         */
        public val platforms: String = "steps.$stepId.outputs.platforms"

        /**
         * Builder nodes metadata
         */
        public val nodes: String = "steps.$stepId.outputs.nodes"

        /**
         * Builder node endpoint (deprecated, use nodes output instead)
         */
        public val endpoint: String = "steps.$stepId.outputs.endpoint"

        /**
         * Builder node status (deprecated, use nodes output instead)
         */
        public val status: String = "steps.$stepId.outputs.status"

        /**
         * Builder node flags (deprecated, use nodes output instead)
         */
        public val flags: String = "steps.$stepId.outputs.flags"
    }
}
