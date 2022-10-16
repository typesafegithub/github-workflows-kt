// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.ericcornelissen

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Git Tag Annotation
 *
 * Get the annotation associated with a git tag
 *
 * [Action on GitHub](https://github.com/ericcornelissen/git-tag-annotation-action)
 */
public class GitTagAnnotationActionV2(
    /**
     * tag of interest (defaults to the GITHUB_REF environment variable)
     */
    public val tag: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<GitTagAnnotationActionV2.Outputs>("ericcornelissen",
        "git-tag-annotation-action", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            tag?.let { "tag" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The git tag annotation
         */
        public val gitTagAnnotation: String = "steps.$stepId.outputs.git-tag-annotation"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
