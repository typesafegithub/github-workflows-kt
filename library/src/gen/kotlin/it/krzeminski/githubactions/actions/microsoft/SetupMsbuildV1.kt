// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.microsoft

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
public data class SetupMsbuildV1(
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
     * The preferred processor architecture of MSBuild. Can be either "x86" or "x64". "x64" is only
     * available from Visual Studio version 17.0 and later.
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
) : ActionWithOutputs<SetupMsbuildV1.Outputs>("microsoft", "setup-msbuild", _customVersion ?: "v1")
        {
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

        public class Custom(
            customStringValue: String,
        ) : SetupMsbuildV1.Architecture(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The resulting location of msbuild for your inputs
         */
        public val msbuildPath: String = "steps.$stepId.outputs.msbuildPath"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
