// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.ActionWithOutputs
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
public class SetupDotnetV3(
    /**
     * Optional SDK version(s) to use. If not provided, will install global.json version when
     * available. Examples: 2.2.104, 3.1, 3.1.x, 3.x
     */
    public val dotnetVersion: String? = null,
    /**
     * Optional quality of the build. The possible values are: daily, signed, validated, preview,
     * ga.
     */
    public val dotnetQuality: SetupDotnetV3.DotNetQuality? = null,
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SetupDotnetV3.Outputs>("actions", "setup-dotnet", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            dotnetVersion?.let { "dotnet-version" to it },
            dotnetQuality?.let { "dotnet-quality" to it.stringValue },
            globalJsonFile?.let { "global-json-file" to it },
            sourceUrl?.let { "source-url" to it },
            owner?.let { "owner" to it },
            configFile?.let { "config-file" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public sealed class DotNetQuality(
        public val stringValue: String,
    ) {
        public object Daily : SetupDotnetV3.DotNetQuality("daily")

        public object Signed : SetupDotnetV3.DotNetQuality("signed")

        public object Validated : SetupDotnetV3.DotNetQuality("validated")

        public object Preview : SetupDotnetV3.DotNetQuality("preview")

        public object Ga : SetupDotnetV3.DotNetQuality("ga")

        public class Custom(
            customStringValue: String,
        ) : SetupDotnetV3.DotNetQuality(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Contains the installed by action .NET SDK version for reuse.
         */
        public val dotnetVersion: String = "steps.$stepId.outputs.dotnet-version"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
