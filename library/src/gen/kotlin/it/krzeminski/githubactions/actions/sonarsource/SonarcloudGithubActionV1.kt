// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.sonarsource

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: SonarCloud Scan
 *
 * Scan your code with SonarCloud to detect bugs, vulnerabilities and code smells in more than 25
 * programming languages.
 *
 *
 * [Action on GitHub](https://github.com/SonarSource/sonarcloud-github-action)
 */
public class SonarcloudGithubActionV1(
    /**
     * Additional arguments to the sonarcloud scanner
     */
    public val args: String? = null,
    /**
     * Set the sonar.projectBaseDir analysis property
     */
    public val projectBaseDir: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("SonarSource", "sonarcloud-github-action", _customVersion ?: "v1.6") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            args?.let { "args" to it },
            projectBaseDir?.let { "projectBaseDir" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
