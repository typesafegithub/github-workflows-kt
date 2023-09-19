// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.anothrnick

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map

/**
 * Action: Github Tag Bump
 *
 * Bump and push git tag on merge
 *
 * [Action on GitHub](https://github.com/anothrNick/github-tag-action)
 */
public data class GithubTagActionV1 private constructor(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<GithubTagActionV1.Outputs>("anothrNick", "github-tag-action", _customVersion ?:
        "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(_customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(_customInputs)

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
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
    }
}
