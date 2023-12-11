// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class SetupDotnetV4 private constructor(
    /**
     * Optional SDK version(s) to use. If not provided, will install global.json version when
     * available. Examples: 2.2.104, 3.1, 3.1.x, 3.x, 6.0.2xx
     */
    public val dotnetVersion: String? = null,
    /**
     * Optional quality of the build. The possible values are: daily, signed, validated, preview,
     * ga.
     */
    public val dotnetQuality: SetupDotnetV4.DotNetQuality? = null,
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
     * Optional input to enable caching of the NuGet global-packages folder
     */
    public val cache: Boolean? = null,
    /**
     * Used to specify the path to a dependency file: packages.lock.json. Supports wildcards or a
     * list of file names for caching multiple dependencies.
     */
    public val cacheDependencyPath: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupDotnetV4.Outputs>("actions", "setup-dotnet", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        dotnetVersion: String? = null,
        dotnetQuality: SetupDotnetV4.DotNetQuality? = null,
        globalJsonFile: String? = null,
        sourceUrl: String? = null,
        owner: String? = null,
        configFile: String? = null,
        cache: Boolean? = null,
        cacheDependencyPath: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(dotnetVersion=dotnetVersion, dotnetQuality=dotnetQuality,
            globalJsonFile=globalJsonFile, sourceUrl=sourceUrl, owner=owner, configFile=configFile,
            cache=cache, cacheDependencyPath=cacheDependencyPath, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            dotnetVersion?.let { "dotnet-version" to it },
            dotnetQuality?.let { "dotnet-quality" to it.stringValue },
            globalJsonFile?.let { "global-json-file" to it },
            sourceUrl?.let { "source-url" to it },
            owner?.let { "owner" to it },
            configFile?.let { "config-file" to it },
            cache?.let { "cache" to it.toString() },
            cacheDependencyPath?.let { "cache-dependency-path" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class DotNetQuality(
        public val stringValue: String,
    ) {
        public object Daily : SetupDotnetV4.DotNetQuality("daily")

        public object Signed : SetupDotnetV4.DotNetQuality("signed")

        public object Validated : SetupDotnetV4.DotNetQuality("validated")

        public object Preview : SetupDotnetV4.DotNetQuality("preview")

        public object Ga : SetupDotnetV4.DotNetQuality("ga")

        public class Custom(
            customStringValue: String,
        ) : SetupDotnetV4.DotNetQuality(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A boolean value to indicate if a cache was hit.
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"

        /**
         * Contains the installed by action .NET SDK version for reuse.
         */
        public val dotnetVersion: String = "steps.$stepId.outputs.dotnet-version"
    }
}
