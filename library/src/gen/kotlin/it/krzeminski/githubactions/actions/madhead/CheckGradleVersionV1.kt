// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: check-gradle-version
 *
 * GitHub Action for Gradle version verification
 *
 * [Action on GitHub](https://github.com/madhead/check-gradle-version)
 */
public class CheckGradleVersionV1(
    /**
     * Relative path to gradlew executable
     */
    public val gradlew: String? = null
) : Action("madhead", "check-gradle-version", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            gradlew?.let { "gradlew" to it },
        ).toTypedArray()
    )
}
