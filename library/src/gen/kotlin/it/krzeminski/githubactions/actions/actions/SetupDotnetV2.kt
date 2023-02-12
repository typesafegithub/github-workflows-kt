// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.domain.actions.Actions

import it.krzeminski.githubactions.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup .NET Core SDK
 *
 * Used to build and publish .NET source. Set up a specific version of the .NET and authentication
 * to private NuGet repository
 *
 * [Action on GitHub](https://github.com/actions/setup-dotnet)
 */
@Deprecated(
    message = "This action has a newer major version: SetupDotnetV3",
    replaceWith = ReplaceWith("SetupDotnetV3"),
)
public data class SetupDotnetV2(
    /**
     * Optional SDK version(s) to use. If not provided, will install global.json version when
     * available. Examples: 2.2.104, 3.1, 3.1.x
     */
    public val dotnetVersion: String? = null,
    /**
     * Optional global.json location, if your global.json isn't located in the root of the repo.
     */
    public val globalJsonFile: String? = null,
    /**
     * Optional package source for which to set up authentication. Will consult any existing
     * NuGet.config in the root of the repo and provide a temporary NuGet.config using the
     * NUGET_AUTH_TOKEN environment variable as a ClearTextPassword
     */
    public val sourceUrl: String? = null,
    /**
     * Optional OWNER for using packages from GitHub Package Registry organizations/users other than
     * the current repository's owner. Only used if a GPR URL is also provided in source-url
     */
    public val owner: String? = null,
    /**
     * Optional NuGet.config location, if your NuGet.config isn't located in the root of the repo.
     */
    public val configFile: String? = null,
    /**
     * Whether prerelease versions should be matched with non-exact versions (for example
     * 5.0.0-preview.6 being matched by 5, 5.0, 5.x or 5.0.x). Defaults to false if not provided.
     */
    public val includePrerelease: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action("actions", "setup-dotnet", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            dotnetVersion?.let { "dotnet-version" to it },
            globalJsonFile?.let { "global-json-file" to it },
            sourceUrl?.let { "source-url" to it },
            owner?.let { "owner" to it },
            configFile?.let { "config-file" to it },
            includePrerelease?.let { "include-prerelease" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
