// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.docker

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
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
@Deprecated(
    message = "This action has a newer major version: SetupBuildxActionV2",
    replaceWith = ReplaceWith("SetupBuildxActionV2"),
)
public class SetupBuildxActionV1(
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
     * Sets up docker build command as an alias to docker buildx
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
     * BuildKit config file
     */
    public val config: String? = null,
    /**
     * Inline BuildKit config
     */
    public val configInline: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SetupBuildxActionV1.Outputs>("docker", "setup-buildx-action", _customVersion
        ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            version?.let { "version" to it },
            driver?.let { "driver" to it },
            driverOpts?.let { "driver-opts" to it.joinToString("\n") },
            buildkitdFlags?.let { "buildkitd-flags" to it },
            install?.let { "install" to it.toString() },
            use?.let { "use" to it.toString() },
            endpoint?.let { "endpoint" to it },
            config?.let { "config" to it },
            configInline?.let { "config-inline" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Builder name
         */
        public val name: String = "steps.$stepId.outputs.name"

        /**
         * Builder driver
         */
        public val driver: String = "steps.$stepId.outputs.driver"

        /**
         * Builder node endpoint
         */
        public val endpoint: String = "steps.$stepId.outputs.endpoint"

        /**
         * Builder node status
         */
        public val status: String = "steps.$stepId.outputs.status"

        /**
         * Builder node flags (if applicable)
         */
        public val flags: String = "steps.$stepId.outputs.flags"

        /**
         * Builder node platforms available (comma separated)
         */
        public val platforms: String = "steps.$stepId.outputs.platforms"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
