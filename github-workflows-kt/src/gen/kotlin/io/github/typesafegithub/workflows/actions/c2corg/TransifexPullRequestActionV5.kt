// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.c2corg

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
 * Action: transifex-pull-request-action
 *
 * Fetches translations from transifex and proposes a pull request to merge these.
 *
 * [Action on GitHub](https://github.com/c2corg/transifex-pull-request-action)
 */
public data class TransifexPullRequestActionV5 private constructor(
    /**
     * Transifex token
     */
    public val transifexToken: String,
    /**
     * Transifex organisation
     */
    public val transifexOrganisation: String,
    /**
     * Transifex project
     */
    public val transifexProject: String,
    /**
     * Transifex resource
     */
    public val transifexResource: String,
    /**
     * Folder where to output files
     */
    public val output: String,
    /**
     * A list of locales to fetch, separated by commas
     */
    public val locales: List<String>,
    /**
     * Github secret
     */
    public val githubToken: String,
    /**
     * Branch used for pull request
     */
    public val branch: String? = null,
    /**
     * The target branch into which the pull request will be merged
     */
    public val baseBranch: String? = null,
    /**
     * Transformation to apply to translation files. One of `none` or `po-to-json`
     */
    public val transform: TransifexPullRequestActionV5.Transform? = null,
    /**
     * Labels to associate to the pull request
     */
    public val labels: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("c2corg", "transifex-pull-request-action", _customVersion ?: "v5")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        transifexToken: String,
        transifexOrganisation: String,
        transifexProject: String,
        transifexResource: String,
        output: String,
        locales: List<String>,
        githubToken: String,
        branch: String? = null,
        baseBranch: String? = null,
        transform: TransifexPullRequestActionV5.Transform? = null,
        labels: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(transifexToken=transifexToken, transifexOrganisation=transifexOrganisation,
            transifexProject=transifexProject, transifexResource=transifexResource, output=output,
            locales=locales, githubToken=githubToken, branch=branch, baseBranch=baseBranch,
            transform=transform, labels=labels, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "transifex_token" to transifexToken,
            "transifex_organisation" to transifexOrganisation,
            "transifex_project" to transifexProject,
            "transifex_resource" to transifexResource,
            "output" to output,
            "locales" to locales.joinToString(","),
            "github_token" to githubToken,
            branch?.let { "branch" to it },
            baseBranch?.let { "base_branch" to it },
            transform?.let { "transform" to it.stringValue },
            labels?.let { "labels" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Transform(
        public val stringValue: String,
    ) {
        public object None : TransifexPullRequestActionV5.Transform("none")

        public object PoToJson : TransifexPullRequestActionV5.Transform("po-to-json")

        public class Custom(
            customStringValue: String,
        ) : TransifexPullRequestActionV5.Transform(customStringValue)
    }
}
