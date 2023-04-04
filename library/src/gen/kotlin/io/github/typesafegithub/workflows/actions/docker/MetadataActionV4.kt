// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.docker

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Docker Metadata action
 *
 * GitHub Action to extract metadata (tags, labels) for Docker
 *
 * [Action on GitHub](https://github.com/docker/metadata-action)
 */
public data class MetadataActionV4 private constructor(
    /**
     * List of Docker images to use as base name for tags
     */
    public val images: List<String>,
    /**
     * List of tags as key-value pair attributes
     */
    public val tags: List<String>? = null,
    /**
     * Flavors to apply
     */
    public val flavor: List<String>? = null,
    /**
     * List of custom labels
     */
    public val labels: List<String>? = null,
    /**
     * Separator to use for tags output (default \n)
     */
    public val sepTags: String? = null,
    /**
     * Separator to use for labels output (default \n)
     */
    public val sepLabels: String? = null,
    /**
     * Bake target name (default docker-metadata-action)
     */
    public val bakeTarget: String? = null,
    /**
     * GitHub Token as provided by secrets
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
) : Action<MetadataActionV4.Outputs>("docker", "metadata-action", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        images: List<String>,
        tags: List<String>? = null,
        flavor: List<String>? = null,
        labels: List<String>? = null,
        sepTags: String? = null,
        sepLabels: String? = null,
        bakeTarget: String? = null,
        githubToken: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(images=images, tags=tags, flavor=flavor, labels=labels, sepTags=sepTags,
            sepLabels=sepLabels, bakeTarget=bakeTarget, githubToken=githubToken,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "images" to images.joinToString("\n"),
            tags?.let { "tags" to it.joinToString("\n") },
            flavor?.let { "flavor" to it.joinToString("\n") },
            labels?.let { "labels" to it.joinToString("\n") },
            sepTags?.let { "sep-tags" to it },
            sepLabels?.let { "sep-labels" to it },
            bakeTarget?.let { "bake-target" to it },
            githubToken?.let { "github-token" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Generated Docker image version
         */
        public val version: String = "steps.$stepId.outputs.version"

        /**
         * Generated Docker tags
         */
        public val tags: String = "steps.$stepId.outputs.tags"

        /**
         * Generated Docker labels
         */
        public val labels: String = "steps.$stepId.outputs.labels"

        /**
         * Bake definiton file
         */
        public val bakeFile: String = "steps.$stepId.outputs.bake-file"

        /**
         * JSON output of tags and labels
         */
        public val json: String = "steps.$stepId.outputs.json"
    }
}
