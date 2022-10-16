// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

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
    public val default: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<ReadJavaProperties.Outputs>("madhead", "read-java-properties", _customVersion
        ?: "latest") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "file" to `file`,
            `property`?.let { "property" to it },
            all?.let { "all" to it.toString() },
            default?.let { "default" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Property's value
         */
        public val `value`: String = "steps.$stepId.outputs.value"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
