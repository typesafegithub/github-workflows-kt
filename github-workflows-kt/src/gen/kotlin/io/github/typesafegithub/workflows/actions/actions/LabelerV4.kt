// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

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
 * Action: Labeler
 *
 * Automatically label new pull requests based on the paths of files being changed
 *
 * [Action on GitHub](https://github.com/actions/labeler)
 *
 * @param repoToken The GitHub token used to manage labels
 * @param configurationPath The path for the label configurations
 * @param syncLabels Whether or not to remove labels when matching files are reverted
 * @param dot Whether or not to auto-include paths starting with dot (e.g. `.github`)
 * @param prNumber The pull request number(s)
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: LabelerV5",
    replaceWith = ReplaceWith("LabelerV5"),
)
public data class LabelerV4 private constructor(
    /**
     * The GitHub token used to manage labels
     */
    public val repoToken: String? = null,
    /**
     * The path for the label configurations
     */
    public val configurationPath: String? = null,
    /**
     * Whether or not to remove labels when matching files are reverted
     */
    public val syncLabels: Boolean? = null,
    /**
     * Whether or not to auto-include paths starting with dot (e.g. `.github`)
     */
    public val dot: Boolean? = null,
    /**
     * The pull request number(s)
     */
    public val prNumber: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<LabelerV4.Outputs>("actions", "labeler", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        repoToken: String? = null,
        configurationPath: String? = null,
        syncLabels: Boolean? = null,
        dot: Boolean? = null,
        prNumber: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(repoToken=repoToken, configurationPath=configurationPath, syncLabels=syncLabels,
            dot=dot, prNumber=prNumber, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            repoToken?.let { "repo-token" to it },
            configurationPath?.let { "configuration-path" to it },
            syncLabels?.let { "sync-labels" to it.toString() },
            dot?.let { "dot" to it.toString() },
            prNumber?.let { "pr-number" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A comma-separated list of all new labels
         */
        public val newLabels: String = "steps.$stepId.outputs.new-labels"

        /**
         * A comma-separated list of all labels that the PR contains
         */
        public val allLabels: String = "steps.$stepId.outputs.all-labels"
    }
}
