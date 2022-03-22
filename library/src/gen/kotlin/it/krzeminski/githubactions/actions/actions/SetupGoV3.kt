// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup Go environment
 *
 * Setup a Go environment and add it to the PATH
 *
 * [Action on GitHub](https://github.com/actions/setup-go)
 */
public class SetupGoV3(
    /**
     * The Go version to download (if necessary) and use. Supports semver spec and ranges.
     */
    public val goVersion: String? = null,
    /**
     * Set this option to true if you want the action to always check for the latest available
     * version that satisfies the version spec
     */
    public val checkLatest: Boolean? = null,
    /**
     * Used to pull node distributions from go-versions.  Since there's a default, this is typically
     * not supplied by the user.
     */
    public val token: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : Action("actions", "setup-go", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            goVersion?.let { "go-version" to it },
            checkLatest?.let { "check-latest" to it.toString() },
            token?.let { "token" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
