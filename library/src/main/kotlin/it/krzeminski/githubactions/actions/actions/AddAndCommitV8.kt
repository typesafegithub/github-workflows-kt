package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

/***
 * You can use this GitHub Action to commit changes made in your workflow run directly to your repo:
 * for example, you use it to lint your code, update documentation, commit updated builds, etc...
 * https://github.com/EndBug/add-and-commit
 */
@Suppress("ConstructorParameterNaming")
data class AddAndCommitV8(
    /** The arguments for the `git add` command */
    val add: String? = null,
    /** The name of the user that will be displayed as the author of the commit. */
    val author_name: String? = null,
    /** The email of the user that will be displayed as the author of the commit. */
    val author_email: String? = null,
    /** The name of the custom committer you want to use,
     * if different from the author of the commit. */
    val committer_name: String? = null,
    /** The email of the custom committer you want to use,
     * if different from the author of the commit. */
    val committer_email: String? = null,
    /** The local path to the directory where your repository is located.
     * You should use actions/checkout first to set it up. */
    val cwd: String? = null,
    /** Determines the way the action fills missing author name and email.
     * Three options are available:
     - github_actor -> UserName <UserName@users.noreply.github.com>
     - user_info -> Your Display Name <your-actual@email.com>
     - github_actions -> github-actions <email associated with the github logo */
    val default_author: String? = null,
    /** The message for the commit. */
    val message: String? = null,
    /** If this input is set, the action will push the commit to a new branch with this name. */
    val new_branch: String? = null,
    /** The way the action should handle pathspec errors from the add and remove commands.
     * Three options are available:
     - ignore -> errors will be logged but the step won't fail
     - exitImmediately -> the action will stop right away, and the step will fail
     - exitAtEnd -> the action will go on, every pathspec error will be logged at the end, the step will fail.
     */
    val pathspec_error_handling: String? = null,
    /** Arguments for the git pull command. By default, the action does not pull. */
    val pull: String? = null,
    /** Whether to push the commit and, if any, its tags to the repo.
     * It can also be used to set the git push arguments  */
    val push: Boolean? = null,
    /** The arguments for the `git rm` command (see the paragraph below for more info) */
    val remove: String? = null,
    /** Arguments for the git tag command
     * (the tag name always needs to be the first word not preceded by an hyphen) */
    val tag: String? = null,
) : Action(
    "EndBug", "add-and-commit", "v8"
) {
    @Suppress("ComplexMethod", "SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            add?.let { "add" to it },
            author_name?.let { "author_name" to it },
            author_email?.let { "author_email" to it },
            committer_name?.let { "committer_name" to it },
            committer_email?.let { "committer_email" to it },
            cwd?.let { "cwd" to it },
            default_author?.let { "default_author" to it },
            message?.let { "message" to it },
            new_branch?.let { "new_branch" to it },
            pathspec_error_handling?.let { "pathspec_error_handling" to it },
            pull?.let { "pull" to it },
            push?.let { "push" to "$it" },
            remove?.let { "remove" to it },
            tag?.let { "tag" to it },
        ).toTypedArray()
    )
}
