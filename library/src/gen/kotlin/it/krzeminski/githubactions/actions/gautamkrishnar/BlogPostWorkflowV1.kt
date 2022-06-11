// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.gautamkrishnar

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Blog Post Workflow
 *
 * Allows you to show your latest blog posts on your github profile or project readme
 *
 * [Action on GitHub](https://github.com/gautamkrishnar/blog-post-workflow)
 */
public class BlogPostWorkflowV1(
    /**
     * GitHub access token with Repo scope
     */
    public val ghToken: String? = null,
    /**
     * Comma separated paths of the readme files you want to update
     */
    public val readmePath: List<String>? = null,
    /**
     * Maximum number of posts you want to show on your readme, all feeds combined
     */
    public val maxPostCount: Int? = null,
    /**
     * Comma separated list of RSS feed urls
     */
    public val feedList: List<String>? = null,
    /**
     * Disables the sorting of list based on publish date
     */
    public val disableSort: Boolean? = null,
    /**
     * Comma separated list of platforms you want to enable the comment filter
     */
    public val filterComments: List<String>? = null,
    /**
     * Inserts newline before the closing tag and after the opening tag when using the template
     * option, for formatting
     */
    public val tagPostPreNewline: Boolean? = null,
    /**
     * Template to use while creating the list of posts. It can contain $title,$url, $newline and
     * $date as variables
     */
    public val template: String? = null,
    /**
     * Allows you to change the format of the date or time displayed when using the $date in the
     * template option
     */
    public val dateFormat: String? = null,
    /**
     * Override the default comment tag name, if you want to show multiple instances of the action
     * on the same repo
     */
    public val commentTagName: String? = null,
    /**
     * Allows you to customize the user agent used by the RSS feed crawler
     */
    public val userAgent: String? = null,
    /**
     * Allows you to customize the accept header of the http request
     */
    public val acceptHeader: String? = null,
    /**
     * Allows you to use the custom tags from your feed items in your template
     */
    public val customTags: List<String>? = null,
    /**
     * Allows you to trim the title in the posts list
     */
    public val titleMaxLength: Int? = null,
    /**
     * Allows you to trim the description in the posts list
     */
    public val descriptionMaxLength: String? = null,
    /**
     * Allows you to execute custom JavaScript code on each item to do advanced text manipulation
     */
    public val itemExec: String? = null,
    /**
     * Commit message used while committing to the repo
     */
    public val commitMessage: String? = null,
    /**
     * Username used while committing to the repo
     */
    public val committerUsername: String? = null,
    /**
     * Email id used while committing to the repo
     */
    public val committerEmail: String? = null,
    /**
     * Prevent updating the readme, instead sets the output to the output variable named `results`
     */
    public val outputOnly: Boolean? = null,
    /**
     * Enables the feature that keeps the repo active by automatically committing to it even though
     * there is no change
     */
    public val enableKeepalive: Boolean? = null,
    /**
     * Maximum number of times to retry the fetch operation if it fails
     */
    public val retryCount: Int? = null,
    /**
     * Time to wait before each retry operation in seconds
     */
    public val retryWaitTime: Int? = null,
    /**
     * Comma separated name of the feeds to show on template
     */
    public val feedNames: List<String>? = null,
    /**
     * Disables html encoding of the feed contents
     */
    public val disableHtmlEncoding: Boolean? = null,
    /**
     * Allows you to specify a template for the individual items in the category list
     */
    public val categoriesTemplate: String? = null,
    /**
     * Disables the validation checks for Title, publish date and URL
     */
    public val disableItemValidation: Boolean? = null,
    /**
     * Enables date filters
     */
    public val filterDates: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<BlogPostWorkflowV1.Outputs>(
    "gautamkrishnar", "blog-post-workflow",
    _customVersion ?: "1.7.2"
) {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            ghToken?.let { "gh_token" to it },
            readmePath?.let { "readme_path" to it.joinToString(",") },
            maxPostCount?.let { "max_post_count" to it.toString() },
            feedList?.let { "feed_list" to it.joinToString(",") },
            disableSort?.let { "disable_sort" to it.toString() },
            filterComments?.let { "filter_comments" to it.joinToString(",") },
            tagPostPreNewline?.let { "tag_post_pre_newline" to it.toString() },
            template?.let { "template" to it },
            dateFormat?.let { "date_format" to it },
            commentTagName?.let { "comment_tag_name" to it },
            userAgent?.let { "user_agent" to it },
            acceptHeader?.let { "accept_header" to it },
            customTags?.let { "custom_tags" to it.joinToString(",") },
            titleMaxLength?.let { "title_max_length" to it.toString() },
            descriptionMaxLength?.let { "description_max_length" to it },
            itemExec?.let { "item_exec" to it },
            commitMessage?.let { "commit_message" to it },
            committerUsername?.let { "committer_username" to it },
            committerEmail?.let { "committer_email" to it },
            outputOnly?.let { "output_only" to it.toString() },
            enableKeepalive?.let { "enable_keepalive" to it.toString() },
            retryCount?.let { "retry_count" to it.toString() },
            retryWaitTime?.let { "retry_wait_time" to it.toString() },
            feedNames?.let { "feed_names" to it.joinToString(",") },
            disableHtmlEncoding?.let { "disable_html_encoding" to it.toString() },
            categoriesTemplate?.let { "categories_template" to it },
            disableItemValidation?.let { "disable_item_validation" to it.toString() },
            filterDates?.let { "filter_dates" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * JSON stringified array of posts
         */
        public val results: String = "steps.$stepId.outputs.results"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
