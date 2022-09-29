// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.anothrnick

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action: Github Tag Bump
 *
 * Bump and push git tag on merge
 *
 * [Action on GitHub](https://github.com/anothrNick/github-tag-action)
 */
public class GithubTagActionV1(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<GithubTagActionV1.Outputs>("anothrNick", "github-tag-action", _customVersion
        ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = LinkedHashMap(_customInputs)

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Generated tag
         */
        public val newTag: String = "steps.$stepId.outputs.new_tag"

        /**
         * The latest tag after running this action
         */
        public val tag: String = "steps.$stepId.outputs.tag"

        /**
         * The part of version which was bumped
         */
        public val part: String = "steps.$stepId.outputs.part"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
