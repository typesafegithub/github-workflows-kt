// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.stefanzweifel

import it.krzeminski.githubactions.domain.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Git Auto Commit
 *
 * Automatically commits files which have been changed during the workflow run and push changes back
 * to remote repository.
 *
 * [Action on GitHub](https://github.com/stefanzweifel/git-auto-commit-action)
 */
public data class GitAutoCommitActionV4(
    /**
     * Commit message
     */
    public val commitMessage: String? = null,
    /**
     * Git branch name, where changes should be pushed too. Required if Action is used on the
     * `pull_request` event
     */
    public val branch: String? = null,
    /**
     * Commit options (eg. --no-verify)
     */
    public val commitOptions: String? = null,
    /**
     * Add options (eg. -u)
     */
    public val addOptions: String? = null,
    /**
     * Status options (eg. --untracked-files=no)
     */
    public val statusOptions: String? = null,
    /**
     * File pattern used for `git add`. For example `src/`&#42;`.js`
     */
    public val filePattern: String? = null,
    /**
     * Local file path to the git repository. Defaults to the current directory (`.`)
     */
    public val repository: String? = null,
    /**
     * Name used for the commit user
     */
    public val commitUserName: String? = null,
    /**
     * Email address used for the commit user
     */
    public val commitUserEmail: String? = null,
    /**
     * Value used for the commit author. Defaults to the username of whoever triggered this workflow
     * run.
     */
    public val commitAuthor: String? = null,
    /**
     * Message used to create a new git tag with the commit. Keep this empty, if no tag should be
     * created.
     */
    public val taggingMessage: String? = null,
    /**
     * Push options (eg. --force)
     */
    public val pushOptions: String? = null,
    /**
     * Skip the check if the git repository is dirty and always try to create a commit.
     */
    public val skipDirtyCheck: Boolean? = null,
    /**
     * Skip the call to git-fetch.
     */
    public val skipFetch: Boolean? = null,
    /**
     * Skip the call to git-checkout.
     */
    public val skipCheckout: Boolean? = null,
    /**
     * Stop the shell from expanding filenames
     * (https://www.gnu.org/software/bash/manual/html_node/Filename-Expansion.html)
     */
    public val disableGlobbing: Boolean? = null,
    /**
     * Create new branch with the name of `branch`-input in local and remote repository, if it
     * doesn't exist yet.
     */
    public val createBranch: Boolean? = null,
    /**
     * Internal use only! Path to git binary used to check if git is available. (Don't change this!)
     */
    public val internalGitBinary: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<GitAutoCommitActionV4.Outputs>("stefanzweifel", "git-auto-commit-action",
        _customVersion ?: "v4") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            commitMessage?.let { "commit_message" to it },
            branch?.let { "branch" to it },
            commitOptions?.let { "commit_options" to it },
            addOptions?.let { "add_options" to it },
            statusOptions?.let { "status_options" to it },
            filePattern?.let { "file_pattern" to it },
            repository?.let { "repository" to it },
            commitUserName?.let { "commit_user_name" to it },
            commitUserEmail?.let { "commit_user_email" to it },
            commitAuthor?.let { "commit_author" to it },
            taggingMessage?.let { "tagging_message" to it },
            pushOptions?.let { "push_options" to it },
            skipDirtyCheck?.let { "skip_dirty_check" to it.toString() },
            skipFetch?.let { "skip_fetch" to it.toString() },
            skipCheckout?.let { "skip_checkout" to it.toString() },
            disableGlobbing?.let { "disable_globbing" to it.toString() },
            createBranch?.let { "create_branch" to it.toString() },
            internalGitBinary?.let { "internal_git_binary" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Value is "true", if the repository was dirty and file changes have been detected. Value
         * is "false", if no changes have been detected.
         */
        public val changesDetected: String = "steps.$stepId.outputs.changes_detected"

        /**
         * Full hash of the created commit. Only present if the "changes_detected" output is "true".
         */
        public val commitHash: String = "steps.$stepId.outputs.commit_hash"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
