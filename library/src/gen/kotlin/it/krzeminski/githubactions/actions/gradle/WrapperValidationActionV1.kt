// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Gradle Wrapper Validation
 *
 * Validates Gradle Wrapper JAR Files
 *
 * [Action on GitHub](https://github.com/gradle/wrapper-validation-action)
 */
public class WrapperValidationActionV1(
    /**
     * Minimum expected wrapper JAR files
     */
    public val minWrapperCount: Int? = null,
    /**
     * Allow snapshot Gradle versions
     */
    public val allowSnapshots: Boolean? = null,
    /**
     * Allow arbitrary checksums, comma separated
     */
    public val allowChecksums: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : Action("gradle", "wrapper-validation-action", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            minWrapperCount?.let { "min-wrapper-count" to it.toString() },
            allowSnapshots?.let { "allow-snapshots" to it.toString() },
            allowChecksums?.let { "allow-checksums" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
