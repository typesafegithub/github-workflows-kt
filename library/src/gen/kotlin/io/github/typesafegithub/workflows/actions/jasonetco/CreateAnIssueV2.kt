// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.jasonetco

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
 * Action: Create an issue
 *
 * Creates a new issue using a template with front matter.
 *
 * [Action on GitHub](https://github.com/JasonEtco/create-an-issue)
 */
public data class CreateAnIssueV2 private constructor(
    /**
     * GitHub handle of the user(s) to assign the issue (comma-separated)
     */
    public val assignees: List<String>? = null,
    /**
     * Number of the milestone to assign the issue to
     */
    public val milestone: String? = null,
    /**
     * The name of the file to use as the issue template
     */
    public val filename: String? = null,
    /**
     * Update an open existing issue with the same title if it exists
     */
    public val updateExisting: Boolean? = null,
    /**
     * Existing types of issues to search for (comma-separated)
     */
    public val searchExisting: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CreateAnIssueV2.Outputs>("JasonEtco", "create-an-issue", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        assignees: List<String>? = null,
        milestone: String? = null,
        filename: String? = null,
        updateExisting: Boolean? = null,
        searchExisting: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(assignees=assignees, milestone=milestone, filename=filename,
            updateExisting=updateExisting, searchExisting=searchExisting,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            assignees?.let { "assignees" to it.joinToString(",") },
            milestone?.let { "milestone" to it },
            filename?.let { "filename" to it },
            updateExisting?.let { "update_existing" to it.toString() },
            searchExisting?.let { "search_existing" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Number of the issue that was created
         */
        public val number: String = "steps.$stepId.outputs.number"

        /**
         * URL of the issue that was created
         */
        public val url: String = "steps.$stepId.outputs.url"
    }
}
