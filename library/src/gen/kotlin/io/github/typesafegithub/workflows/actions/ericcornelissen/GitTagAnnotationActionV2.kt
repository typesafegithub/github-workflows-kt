// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.ericcornelissen

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class GitTagAnnotationActionV2 private constructor(
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
    public val _customVersion: String? = null,
) : Action<GitTagAnnotationActionV2.Outputs>("ericcornelissen", "git-tag-annotation-action",
        _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        tag: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(tag=tag, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            tag?.let { "tag" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The git tag annotation
         */
        public val gitTagAnnotation: String = "steps.$stepId.outputs.git-tag-annotation"
    }
}
