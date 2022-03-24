// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.`10up`

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: WordPress Plugin Deploy
 *
 * Deploy to the WordPress Plugin Repository
 *
 * [Action on GitHub](https://github.com/10up/action-wordpress-plugin-deploy)
 */
public class ActionWordpressPluginDeployV2(
    /**
     * Generate package zip file?
     */
    public val generateZip: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null
) : ActionWithOutputs<ActionWordpressPluginDeployV2.Outputs>(
    "10up",
    "action-wordpress-plugin-deploy", _customVersion ?: "v2.0.0"
) {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            generateZip?.let { "generate-zip" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Path to zip file
         */
        public val zipPath: String = "steps.$stepId.outputs.zip-path"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
