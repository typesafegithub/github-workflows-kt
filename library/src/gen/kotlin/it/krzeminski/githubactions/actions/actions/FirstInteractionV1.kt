// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: First interaction
 *
 * Greet new contributors when they create their first issue or open their first pull request
 *
 * [Action on GitHub](https://github.com/actions/first-interaction)
 */
public class FirstInteractionV1(
    /**
     * Token for the repository. Can be passed in using {{ secrets.GITHUB_TOKEN }}
     */
    public val repoToken: String,
    /**
     * Comment to post on an individual's first issue
     */
    public val issueMessage: String? = null,
    /**
     * Comment to post on an individual's first pull request
     */
    public val prMessage: String? = null
) : Action("actions", "first-interaction", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "repo-token" to repoToken,
            issueMessage?.let { "issue-message" to it },
            prMessage?.let { "pr-message" to it },
        ).toTypedArray()
    )
}
