// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.microsoft

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
 * Action: setup-msbuild
 *
 * Helps set up MSBuild into PATH for later usage.
 *
 * [Action on GitHub](https://github.com/microsoft/setup-msbuild)
 *
 * @param vswherePath Folder location of where vswhere.exe is located if a self-hosted agent
 * @param vsVersion Version of Visual Studio to search; defaults to latest if not specified
 * @param vsPrerelease Enable searching for pre-release versions of Visual Studio/MSBuild
 * @param msbuildArchitecture The preferred processor architecture of MSBuild. Can be either "x86",
 * "x64", or "arm64". "x64" is only available from Visual Studio version 17.0 and later.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class SetupMsbuildV2 private constructor(
    /**
     * Folder location of where vswhere.exe is located if a self-hosted agent
     */
    public val vswherePath: String? = null,
    /**
     * Version of Visual Studio to search; defaults to latest if not specified
     */
    public val vsVersion: String? = null,
    /**
     * Enable searching for pre-release versions of Visual Studio/MSBuild
     */
    public val vsPrerelease: Boolean? = null,
    /**
     * The preferred processor architecture of MSBuild. Can be either "x86", "x64", or "arm64".
     * "x64" is only available from Visual Studio version 17.0 and later.
     */
    public val msbuildArchitecture: SetupMsbuildV2.Architecture? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupMsbuildV2.Outputs>("microsoft", "setup-msbuild", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        vswherePath: String? = null,
        vsVersion: String? = null,
        vsPrerelease: Boolean? = null,
        msbuildArchitecture: SetupMsbuildV2.Architecture? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(vswherePath=vswherePath, vsVersion=vsVersion, vsPrerelease=vsPrerelease,
            msbuildArchitecture=msbuildArchitecture, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            vswherePath?.let { "vswhere-path" to it },
            vsVersion?.let { "vs-version" to it },
            vsPrerelease?.let { "vs-prerelease" to it.toString() },
            msbuildArchitecture?.let { "msbuild-architecture" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X86 : SetupMsbuildV2.Architecture("x86")

        public object X64 : SetupMsbuildV2.Architecture("x64")

        public object Arm64 : SetupMsbuildV2.Architecture("arm64")

        public class Custom(
            customStringValue: String,
        ) : SetupMsbuildV2.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The resulting location of msbuild for your inputs
         */
        public val msbuildPath: String = "steps.$stepId.outputs.msbuildPath"
    }
}
