// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: First interaction
 *
 * Greet new contributors when they create their first issue or open their first pull request
 *
 * [Action on GitHub](https://github.com/actions/first-interaction)
 */
public data class FirstInteractionV1(
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
    public val prMessage: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("actions", "first-interaction", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "repo-token" to repoToken,
            issueMessage?.let { "issue-message" to it },
            prMessage?.let { "pr-message" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
