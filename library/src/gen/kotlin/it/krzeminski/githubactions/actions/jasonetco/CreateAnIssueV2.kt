// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.jasonetco

import it.krzeminski.githubactions.domain.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
public data class CreateAnIssueV2(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<CreateAnIssueV2.Outputs>("JasonEtco", "create-an-issue", _customVersion ?:
        "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            assignees?.let { "assignees" to it.joinToString(",") },
            milestone?.let { "milestone" to it },
            filename?.let { "filename" to it },
            updateExisting?.let { "update_existing" to it.toString() },
            searchExisting?.let { "search_existing" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Number of the issue that was created
         */
        public val number: String = "steps.$stepId.outputs.number"

        /**
         * URL of the issue that was created
         */
        public val url: String = "steps.$stepId.outputs.url"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
