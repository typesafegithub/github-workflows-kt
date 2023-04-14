// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.microsoft

import io.github.typesafegithub.workflows.domain.actions.Action
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
 */
public data class SetupMsbuildV1 private constructor(
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
    public val msbuildArchitecture: SetupMsbuildV1.Architecture? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<SetupMsbuildV1.Outputs>("microsoft", "setup-msbuild", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        vswherePath: String? = null,
        vsVersion: String? = null,
        vsPrerelease: Boolean? = null,
        msbuildArchitecture: SetupMsbuildV1.Architecture? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(vswherePath=vswherePath, vsVersion=vsVersion, vsPrerelease=vsPrerelease,
            msbuildArchitecture=msbuildArchitecture, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            vswherePath?.let { "vswhere-path" to it },
            vsVersion?.let { "vs-version" to it },
            vsPrerelease?.let { "vs-prerelease" to it.toString() },
            msbuildArchitecture?.let { "msbuild-architecture" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X86 : SetupMsbuildV1.Architecture("x86")

        public object X64 : SetupMsbuildV1.Architecture("x64")

        public object Arm64 : SetupMsbuildV1.Architecture("arm64")

        public class Custom(
            customStringValue: String,
        ) : SetupMsbuildV1.Architecture(customStringValue)
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
