// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.w9jds

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Deprecated
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
@Deprecated(
    message = "This action has a newer major version: FirebaseActionV11",
    replaceWith = ReplaceWith("FirebaseActionV11"),
)
public class FirebaseActionV2(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<FirebaseActionV2.Outputs>("w9jds", "firebase-action", _customVersion ?:
        "v2.2.2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> =
            LinkedHashMap(_customInputs)

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Response from the firebase command executed
         */
        public val response: String = "steps.$stepId.outputs.response"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
