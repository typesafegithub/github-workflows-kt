// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.jasonetco

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Create an issue
 *
 * Creates a new issue using a template with front matter.
 *
 * [Action on GitHub](https://github.com/JasonEtco/create-an-issue)
 */
public class CreateAnIssueV2(
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
    public val updateExisting: String? = null,
    /**
     * Existing types of issues to search for (comma-separated)
     */
    public val searchExisting: List<String>? = null
) : ActionWithOutputs<CreateAnIssueV2.Outputs>("JasonEtco", "create-an-issue", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            assignees?.let { "assignees" to it.joinToString(",") },
            milestone?.let { "milestone" to it },
            filename?.let { "filename" to it },
            updateExisting?.let { "update_existing" to it },
            searchExisting?.let { "search_existing" to it.joinToString(",") },
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Number of the issue that was created
         */
        public val number: String = "steps.$stepId.outputs.number"

        /**
         * URL of the issue that was created
         */
        public val url: String = "steps.$stepId.outputs.url"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
