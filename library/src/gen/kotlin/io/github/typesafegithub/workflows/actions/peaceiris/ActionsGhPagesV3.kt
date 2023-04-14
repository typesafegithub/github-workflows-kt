// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.peaceiris

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
 * Action: GitHub Pages action
 *
 * GitHub Actions for GitHub Pages 🚀 Deploy static files and publish your site easily.
 * Static-Site-Generators-friendly.
 *
 * [Action on GitHub](https://github.com/peaceiris/actions-gh-pages)
 */
public data class ActionsGhPagesV3 private constructor(
    /**
     * Set a SSH private key from repository secret value for pushing to the remote branch.
     */
    public val deployKey: String? = null,
    /**
     * Set a generated GITHUB_TOKEN for pushing to the remote branch.
     */
    public val githubToken: String? = null,
    /**
     * Set a personal access token for pushing to the remote branch.
     */
    public val personalToken: String? = null,
    /**
     * Set a target branch for deployment.
     */
    public val publishBranch: String? = null,
    /**
     * Set an input directory for deployment.
     */
    public val publishDir: String? = null,
    /**
     * Set an destination subdirectory for deployment.
     */
    public val destinationDir: String? = null,
    /**
     * Set an external repository (owner/repo).
     */
    public val externalRepository: String? = null,
    /**
     * If empty commits should be made to the publication branch
     */
    public val allowEmptyCommit: Boolean? = null,
    /**
     * If existing files in the publish branch should be not removed before deploying
     */
    public val keepFiles: Boolean? = null,
    /**
     * Keep only the latest commit on a GitHub Pages branch
     */
    public val forceOrphan: Boolean? = null,
    /**
     * Set Git user.name
     */
    public val userName: String? = null,
    /**
     * Set Git user.email
     */
    public val userEmail: String? = null,
    /**
     * Set a custom commit message with a triggered commit hash
     */
    public val commitMessage: String? = null,
    /**
     * Set a custom full commit message without a triggered commit hash
     */
    public val fullCommitMessage: String? = null,
    /**
     * Set tag name
     */
    public val tagName: String? = null,
    /**
     * Set tag message
     */
    public val tagMessage: String? = null,
    /**
     * Enable the GitHub Pages built-in Jekyll
     */
    public val enableJekyll: Boolean? = null,
    /**
     * An alias for enable_jekyll to disable adding .nojekyll file to a publishing branch
     */
    public val disableNojekyll: Boolean? = null,
    /**
     * Set custom domain
     */
    public val cname: String? = null,
    /**
     * Set files or directories to exclude from a publish directory.
     */
    public val excludeAssets: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("peaceiris", "actions-gh-pages", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        deployKey: String? = null,
        githubToken: String? = null,
        personalToken: String? = null,
        publishBranch: String? = null,
        publishDir: String? = null,
        destinationDir: String? = null,
        externalRepository: String? = null,
        allowEmptyCommit: Boolean? = null,
        keepFiles: Boolean? = null,
        forceOrphan: Boolean? = null,
        userName: String? = null,
        userEmail: String? = null,
        commitMessage: String? = null,
        fullCommitMessage: String? = null,
        tagName: String? = null,
        tagMessage: String? = null,
        enableJekyll: Boolean? = null,
        disableNojekyll: Boolean? = null,
        cname: String? = null,
        excludeAssets: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(deployKey=deployKey, githubToken=githubToken, personalToken=personalToken,
            publishBranch=publishBranch, publishDir=publishDir, destinationDir=destinationDir,
            externalRepository=externalRepository, allowEmptyCommit=allowEmptyCommit,
            keepFiles=keepFiles, forceOrphan=forceOrphan, userName=userName, userEmail=userEmail,
            commitMessage=commitMessage, fullCommitMessage=fullCommitMessage, tagName=tagName,
            tagMessage=tagMessage, enableJekyll=enableJekyll, disableNojekyll=disableNojekyll,
            cname=cname, excludeAssets=excludeAssets, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            deployKey?.let { "deploy_key" to it },
            githubToken?.let { "github_token" to it },
            personalToken?.let { "personal_token" to it },
            publishBranch?.let { "publish_branch" to it },
            publishDir?.let { "publish_dir" to it },
            destinationDir?.let { "destination_dir" to it },
            externalRepository?.let { "external_repository" to it },
            allowEmptyCommit?.let { "allow_empty_commit" to it.toString() },
            keepFiles?.let { "keep_files" to it.toString() },
            forceOrphan?.let { "force_orphan" to it.toString() },
            userName?.let { "user_name" to it },
            userEmail?.let { "user_email" to it },
            commitMessage?.let { "commit_message" to it },
            fullCommitMessage?.let { "full_commit_message" to it },
            tagName?.let { "tag_name" to it },
            tagMessage?.let { "tag_message" to it },
            enableJekyll?.let { "enable_jekyll" to it.toString() },
            disableNojekyll?.let { "disable_nojekyll" to it.toString() },
            cname?.let { "cname" to it },
            excludeAssets?.let { "exclude_assets" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
