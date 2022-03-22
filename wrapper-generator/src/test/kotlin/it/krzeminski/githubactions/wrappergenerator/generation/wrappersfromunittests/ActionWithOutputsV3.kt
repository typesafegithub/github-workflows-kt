// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-outputs)
 */
public class ActionWithOutputsV3(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : ActionWithOutputs<ActionWithOutputsV3.Outputs>("john-smith", "action-with-outputs", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Cool output!
         */
        public val bazGoo: String = "steps.$stepId.outputs.baz-goo"

        /**
         * Another output...
         */
        public val looWoz: String = "steps.$stepId.outputs.loo-woz"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
