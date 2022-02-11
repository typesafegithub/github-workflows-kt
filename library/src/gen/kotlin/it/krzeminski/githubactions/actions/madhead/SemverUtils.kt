// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: semver-utils
 *
 * One-stop shop for working with semantic versions in your workflows
 *
 * https://github.com/madhead/semver-utils
 */
public class SemverUtils(
    /**
     * A version to process
     */
    public val version: String,
    /**
     * A version to compare with, if any
     */
    public val compareTo: String? = null,
    /**
     * A range to check against
     */
    public val satisfies: String? = null,
    /**
     * An identifier to pass to the semver's inc function
     */
    public val identifier: String? = null
) : Action("madhead", "semver-utils", "latest") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "version" to version,
            compareTo?.let { "compare-to" to it },
            satisfies?.let { "satisfies" to it },
            identifier?.let { "identifier" to it },
        ).toTypedArray()
    )
}
