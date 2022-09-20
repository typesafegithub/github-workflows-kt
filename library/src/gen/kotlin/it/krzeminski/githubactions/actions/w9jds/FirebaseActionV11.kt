// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.w9jds

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action: GitHub Action for Firebase
 *
 * Wraps the firebase-tools CLI to enable common commands.
 *
 * [Action on GitHub](https://github.com/w9jds/firebase-action)
 */
public class FirebaseActionV11(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<FirebaseActionV11.Outputs>("w9jds", "firebase-action", _customVersion ?:
        "v11.9.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = LinkedHashMap(_customInputs)

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Response from the firebase command executed
         */
        public val response: String = "steps.$stepId.outputs.response"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
