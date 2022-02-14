// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.endbug

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: Add & Commit
 *
 * Automatically commit changes made in your workflow run directly to your repo
 *
 * [Action on GitHub](https://github.com/EndBug/add-and-commit)
 */
public class AddAndCommitV8(
    /**
     * Arguments for the git add command
     */
    public val add: String? = null,
    /**
     * The name of the user that will be displayed as the author of the commit
     */
    public val authorName: String? = null,
    /**
     * The email of the user that will be displayed as the author of the commit
     */
    public val authorEmail: String? = null,
    /**
     * Additional arguments for the git commit command
     */
    public val commit: String? = null,
    /**
     * The name of the custom committer you want to use
     */
    public val committerName: String? = null,
    /**
     * The email of the custom committer you want to use
     */
    public val committerEmail: String? = null,
    /**
     * The directory where your repository is located. You should use actions/checkout first to set
     * it up
     */
    public val cwd: String? = null,
    /**
     * How the action should fill missing author name or email.
     */
    public val defaultAuthor: AddAndCommitV8.DefaultActor? = null,
    /**
     * The message for the commit
     */
    public val message: String? = null,
    /**
     * The name of the branch to create.
     */
    public val newBranch: String? = null,
    /**
     * The way the action should handle pathspec errors from the add and remove commands.
     */
    public val pathspecErrorHandling: AddAndCommitV8.PathSpecErrorHandling? = null,
    /**
     * Arguments for the git pull command. By default, the action does not pull.
     */
    public val pull: String? = null,
    /**
     * Whether to push the commit and, if any, its tags to the repo. It can also be used to set the
     * git push arguments (more info in the README)
     */
    public val push: String? = null,
    /**
     * Arguments for the git rm command
     */
    public val remove: String? = null,
    /**
     * Arguments for the git tag command (the tag name always needs to be the first word not
     * preceded by a hyphen)
     */
    public val tag: String? = null,
    /**
     * The token used to make requests to the GitHub API. It's NOT used to make commits and should
     * not be changed.
     */
    public val githubToken: String? = null
) : Action("EndBug", "add-and-commit", "v8") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            add?.let { "add" to it },
            authorName?.let { "author_name" to it },
            authorEmail?.let { "author_email" to it },
            commit?.let { "commit" to it },
            committerName?.let { "committer_name" to it },
            committerEmail?.let { "committer_email" to it },
            cwd?.let { "cwd" to it },
            defaultAuthor?.let { "default_author" to it.stringValue },
            message?.let { "message" to it },
            newBranch?.let { "new_branch" to it },
            pathspecErrorHandling?.let { "pathspec_error_handling" to it.stringValue },
            pull?.let { "pull" to it },
            push?.let { "push" to it },
            remove?.let { "remove" to it },
            tag?.let { "tag" to it },
            githubToken?.let { "github_token" to it },
        ).toTypedArray()
    )

    public sealed class DefaultActor(
        public val stringValue: String
    ) {
        public object GithubActor : AddAndCommitV8.DefaultActor("github_actor")

        public object UserInfo : AddAndCommitV8.DefaultActor("user_info")

        public object GithubActions : AddAndCommitV8.DefaultActor("github_actions")

        public class Custom(
            customStringValue: String
        ) : AddAndCommitV8.DefaultActor(customStringValue)
    }

    public sealed class PathSpecErrorHandling(
        public val stringValue: String
    ) {
        public object Ignore : AddAndCommitV8.PathSpecErrorHandling("ignore")

        public object ExitImmediately : AddAndCommitV8.PathSpecErrorHandling("exitImmediately")

        public object ExitAtEnd : AddAndCommitV8.PathSpecErrorHandling("exitAtEnd")

        public class Custom(
            customStringValue: String
        ) : AddAndCommitV8.PathSpecErrorHandling(customStringValue)
    }
}
