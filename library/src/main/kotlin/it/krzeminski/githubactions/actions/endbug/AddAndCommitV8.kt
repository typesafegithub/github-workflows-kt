package it.krzeminski.githubactions.actions.endbug

import it.krzeminski.githubactions.actions.Action

/***
 * You can use this GitHub Action to commit changes made in your workflow run directly to your repo:
 * for example, you use it to lint your code, update documentation, commit updated builds, etc...
 * https://github.com/EndBug/add-and-commit
 */
@Suppress("LongParameterList")
class AddAndCommitV8(
    /** The arguments for the `git add` command */
    val add: String? = null,
    /** The name of the user that will be displayed as the author of the commit. */
    val authorName: String? = null,
    /** The email of the user that will be displayed as the author of the commit. */
    val authorEmail: String? = null,
    /** Additional arguments for the git commit command */
    val commit: String? = null,
    /** The name of the custom committer you want to use,
     * if different from the author of the commit. */
    val committerName: String? = null,
    /** The email of the custom committer you want to use,
     * if different from the author of the commit. */
    val committerEmail: String? = null,
    /** The local path to the directory where your repository is located.
     * You should use actions/checkout first to set it up. */
    val cwd: String? = null,
    /** Determines the way the action fills missing author name and email. **/
    val defaultAuthor: DefaultActor? = null,
    /** The message for the commit. */
    val message: String? = null,
    /** If this input is set, the action will push the commit to a new branch with this name. */
    val newBranch: String? = null,
    /** The way the action should handle pathspec errors from the add and remove commands.*/
    val pathspecErrorHandling: PathSpecErrorHandling? = null,
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
            authorName?.let { "author_name" to it },
            authorEmail?.let { "author_email" to it },
            commit?.let { "commit" to it },
            committerName?.let { "committer_name" to it },
            committerEmail?.let { "committer_email" to it },
            cwd?.let { "cwd" to it },
            defaultAuthor?.let { "default_author" to it.yaml },
            message?.let { "message" to it },
            newBranch?.let { "new_branch" to it },
            pathspecErrorHandling?.let { "pathspec_error_handling" to it.yaml },
            pull?.let { "pull" to it },
            push?.let { "push" to "$it" },
            remove?.let { "remove" to it },
            tag?.let { "tag" to it },
        ).toTypedArray()
    )

    sealed class DefaultActor(val yaml: String) {
        /** UserName <UserName@users.noreply.github.com> */
        object GithubActor : DefaultActor("github_actor")

        /** Your Display Name <your-actual@email.com> */
        object UserInfo : DefaultActor("user_info")

        /** github-actions <email associated with the github logo> */
        object GitHubActions : DefaultActor("github_actions")

        class Custom(yaml: String) : DefaultActor(yaml)
    }

    sealed class PathSpecErrorHandling(val yaml: String) {
        /* errors will be logged but the step won't fail */
        object Ignore : PathSpecErrorHandling("ignore")

        /** the action will stop right away, and the step will fail */
        object ExitImmediatly : PathSpecErrorHandling("exitImmediately")

        /** the action will go on, every pathspec error will be logged at the end, the step will fail. */
        object ExitAtEnd : PathSpecErrorHandling("exitAtEnd")

        class Custom(yaml: String) : PathSpecErrorHandling(yaml)
    }
}
