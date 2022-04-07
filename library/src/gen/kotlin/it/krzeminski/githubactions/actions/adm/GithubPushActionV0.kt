// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.adm

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: GitHub Push
 *
 * Pushing to GitHub repository local changes
 *
 * [Action on GitHub](https://github.com/ad-m/github-push-action)
 */
public class GithubPushActionV0(
    /**
     * Token for the repo. Can be passed in using $\{{ secrets.GITHUB_TOKEN }}
     */
    public val githubToken: String,
    /**
     * Repository name to push. Default or empty value represents current github repository
     * (${GITHUB_REPOSITORY})
     */
    public val repository: String? = null,
    /**
     * Destination branch to push changes
     */
    public val branch: String? = null,
    /**
     * Determines if force push is used
     */
    public val force: Boolean? = null,
    /**
     * Determines if --tags is used
     */
    public val tags: Boolean? = null,
    /**
     * Directory to change to before pushing.
     */
    public val directory: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("ad-m", "github-push-action", _customVersion ?: "v0.6.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "github_token" to githubToken,
            repository?.let { "repository" to it },
            branch?.let { "branch" to it },
            force?.let { "force" to it.toString() },
            tags?.let { "tags" to it.toString() },
            directory?.let { "directory" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
