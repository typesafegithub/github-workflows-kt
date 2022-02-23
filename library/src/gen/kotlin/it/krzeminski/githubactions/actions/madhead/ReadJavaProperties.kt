// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress

/**
 * Action: read-java-properties
 *
 * GitHub Action to read values from Java's .properties files
 *
 * [Action on GitHub](https://github.com/madhead/read-java-properties)
 */
public class ReadJavaProperties(
    /**
     * A file to read
     */
    public val `file`: String,
    /**
     * A property to read from the file
     */
    public val `property`: String? = null,
    /**
     * A flag to read and output all the properties from the file
     */
    public val all: Boolean? = null,
    /**
     * A value to return in case of any errors
     */
    public val default: String? = null
) : ActionWithOutputs<ReadJavaProperties.Outputs>("madhead", "read-java-properties", "latest") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "file" to `file`,
            `property`?.let { "property" to it },
            all?.let { "all" to it.toString() },
            default?.let { "default" to it },
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Property's value
         */
        public val `value`: String = "steps.$stepId.outputs.value"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
