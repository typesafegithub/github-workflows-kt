// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.microsoft

import it.krzeminski.githubactions.actions.ActionWithOutputs
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
public class SetupMsbuildV1(
    /**
     * Folder location of where vswhere.exe is located if a self-hosted agent
     */
    public val vswherePath: String? = null,
    /**
     * Version of Visual Studio to search; defaults to latest if not specified
     */
    public val vsVersion: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SetupMsbuildV1.Outputs>("microsoft", "setup-msbuild", _customVersion ?: "v1")
        {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            vswherePath?.let { "vswhere-path" to it },
            vsVersion?.let { "vs-version" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The resulting location of msbuild for your inputs
         */
        public val msbuildPath: String = "steps.$stepId.outputs.msbuildPath"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
