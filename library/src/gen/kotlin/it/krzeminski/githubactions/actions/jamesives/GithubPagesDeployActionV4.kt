// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.jamesives

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Deploy to GitHub Pages
 *
 * This action will handle the deployment process of your project to GitHub Pages.
 *
 * [Action on GitHub](https://github.com/JamesIves/github-pages-deploy-action)
 */
public class GithubPagesDeployActionV4(
    /**
     * This option allows you to define a private SSH key to be used in conjunction with a
     * repository deployment key to deploy using SSH. The private key should be stored in the `secrets
     * / with` menu **as a secret**. The public should be stored in the repositories deployment keys
     * menu and be given write access.
     * Alternatively you can set this field to `true` to enable SSH endpoints for deployment without
     * configuring the ssh client. This can be useful if you've already setup the SSH client using
     * another package or action in a previous step.
     */
    public val sshKey: String? = null,
    /**
     * This option defaults to the repository scoped GitHub Token.  However if you need more
     * permissions for things such as deploying to another repository, you can add a Personal Access
     * Token (PAT) here.  This should be stored in the `secrets / with` menu **as a secret**.
     * We recommend using a service account with the least permissions necessary and when generating
     * a new PAT that you select the least permission scopes required.
     * [Learn more about creating and using encrypted secrets
     * here.](https://help.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets)
     */
    public val token: String? = null,
    /**
     * This is the branch you wish to deploy to, for example gh-pages or docs.
     */
    public val branch: String? = null,
    /**
     * The folder in your repository that you want to deploy. If your build script compiles into a
     * directory named build you would put it here. Folder paths cannot have a leading / or ./. If you
     * wish to deploy the root directory you can place a . here.
     */
    public val folder: String,
    /**
     * If you would like to push the contents of the deployment folder into a specific directory on
     * the deployment branch you can specify it here.
     */
    public val targetFolder: String? = null,
    /**
     * If you need to customize the commit message for an integration you can do so.
     */
    public val commitMessage: String? = null,
    /**
     * If your project generates hashed files on build you can use this option to automatically
     * delete them from the target folder on the deployment branch with each deploy. This option is on
     * by default and can be toggled off by setting it to false.
     */
    public val clean: Boolean? = null,
    /**
     * If you need to use clean but you would like to preserve certain files or folders you can use
     * this option. This should contain each pattern as a single line in a multiline string.
     */
    public val cleanExclude: List<String>? = null,
    /**
     * Do not actually push back, but use `--dry-run` on `git push` invocations instead.
     */
    public val dryRun: Boolean? = null,
    /**
     * Whether to force-push and overwrite any existing deployment. Setting this to false will
     * attempt to rebase simultaneous deployments. This option is on by default and can be toggled off
     * by setting it to false.
     */
    public val force: Boolean? = null,
    /**
     * Allows you to customize the name that is attached to the GitHub config which is used when
     * pushing the deployment commits. If this is not included it will use the name in the GitHub
     * context, followed by the name of the action.
     */
    public val gitConfigName: String? = null,
    /**
     * Allows you to customize the email that is attached to the GitHub config which is used when
     * pushing the deployment commits. If this is not included it will use the email in the GitHub
     * context, followed by a generic noreply GitHub email.
     */
    public val gitConfigEmail: String? = null,
    /**
     * Allows you to specify a different repository path so long as you have permissions to push to
     * it. This should be formatted like so: JamesIves/github-pages-deploy-action
     */
    public val repositoryName: String? = null,
    /**
     * Add a tag to the commit, this can be used like so: 'v0.1'. Only works when 'dry-run' is not
     * used.
     */
    public val tag: String? = null,
    /**
     * This option can be used if you'd prefer to have a single commit on the deployment branch
     * instead of maintaining the full history.
     */
    public val singleCommit: Boolean? = null,
    /**
     * Silences the action output preventing it from displaying git messages.
     */
    public val silent: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<GithubPagesDeployActionV4.Outputs>("JamesIves", "github-pages-deploy-action",
        _customVersion ?: "v4") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            sshKey?.let { "ssh-key" to it },
            token?.let { "token" to it },
            branch?.let { "branch" to it },
            "folder" to folder,
            targetFolder?.let { "target-folder" to it },
            commitMessage?.let { "commit-message" to it },
            clean?.let { "clean" to it.toString() },
            cleanExclude?.let { "clean-exclude" to it.joinToString("\n") },
            dryRun?.let { "dry-run" to it.toString() },
            force?.let { "force" to it.toString() },
            gitConfigName?.let { "git-config-name" to it },
            gitConfigEmail?.let { "git-config-email" to it },
            repositoryName?.let { "repository-name" to it },
            tag?.let { "tag" to it },
            singleCommit?.let { "single-commit" to it.toString() },
            silent?.let { "silent" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The status of the deployment that indicates if the run failed or passed. Possible outputs
         * include: success|failed|skipped
         */
        public val deploymentStatus: String = "steps.$stepId.outputs.deployment-status"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
