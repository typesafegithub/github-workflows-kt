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
public data class MetadataActionV5 private constructor(
    /**
     * Where to get context data. Allowed options are "workflow"  (default), "git".
     */
    public val context: MetadataActionV5.Context? = null,
    /**
     * List of Docker images to use as base name for tags
     */
    public val images: List<String>? = null,
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
     * List of custom annotations
     */
    public val annotations: String? = null,
    /**
     * Separator to use for tags output (default \n)
     */
    public val sepTags: String? = null,
    /**
     * Separator to use for labels output (default \n)
     */
    public val sepLabels: String? = null,
    /**
     * Separator to use for annotations output (default \n)
     */
    public val sepAnnotations: String? = null,
    /**
     * Bake target name (default docker-metadata-action)
     */
    public val bakeTarget: String? = null,
    /**
     * GitHub Token as provided by secrets
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
) : RegularAction<MetadataActionV5.Outputs>("docker", "metadata-action", _customVersion ?: "v5") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        context: MetadataActionV5.Context? = null,
        images: List<String>? = null,
        tags: List<String>? = null,
        flavor: List<String>? = null,
        labels: List<String>? = null,
        annotations: String? = null,
        sepTags: String? = null,
        sepLabels: String? = null,
        sepAnnotations: String? = null,
        bakeTarget: String? = null,
        githubToken: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(context=context, images=images, tags=tags, flavor=flavor, labels=labels,
            annotations=annotations, sepTags=sepTags, sepLabels=sepLabels,
            sepAnnotations=sepAnnotations, bakeTarget=bakeTarget, githubToken=githubToken,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            context?.let { "context" to it.stringValue },
            images?.let { "images" to it.joinToString("\n") },
            tags?.let { "tags" to it.joinToString("\n") },
            flavor?.let { "flavor" to it.joinToString("\n") },
            labels?.let { "labels" to it.joinToString("\n") },
            annotations?.let { "annotations" to it },
            sepTags?.let { "sep-tags" to it },
            sepLabels?.let { "sep-labels" to it },
            sepAnnotations?.let { "sep-annotations" to it },
            bakeTarget?.let { "bake-target" to it },
            githubToken?.let { "github-token" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Context(
        public val stringValue: String,
    ) {
        public object Workflow : MetadataActionV5.Context("workflow")

        public object Git : MetadataActionV5.Context("git")

        public class Custom(
            customStringValue: String,
        ) : MetadataActionV5.Context(customStringValue)
    }

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
         * Generated annotations
         */
        public val annotations: String = "steps.$stepId.outputs.annotations"

        /**
         * JSON output of tags and labels
         */
        public val json: String = "steps.$stepId.outputs.json"

        /**
         * Bake definition file with tags
         */
        public val bakeFileTags: String = "steps.$stepId.outputs.bake-file-tags"

        /**
         * Bake definition file with labels
         */
        public val bakeFileLabels: String = "steps.$stepId.outputs.bake-file-labels"

        /**
         * Bake definiton file with annotations
         */
        public val bakeFileAnnotations: String = "steps.$stepId.outputs.bake-file-annotations"

        /**
         * Bake definition file with tags and labels
         */
        public val bakeFile: String = "steps.$stepId.outputs.bake-file"
    }
}
