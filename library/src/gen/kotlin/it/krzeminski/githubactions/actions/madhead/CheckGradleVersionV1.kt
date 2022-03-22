// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: check-gradle-version
 *
 * GitHub Action for Gradle version verification
 *
 * [Action on GitHub](https://github.com/madhead/check-gradle-version)
 */
public class CheckGradleVersionV1(
    /**
     * Relative path to gradlew executable
     */
    public val gradlew: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customArguments: Map<String, String> = mapOf()
) : ActionWithOutputs<CheckGradleVersionV1.Outputs>("madhead", "check-gradle-version", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            gradlew?.let { "gradlew" to it },
            *_customArguments.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * Project Gradle version
         */
        public val version: String = "steps.$stepId.outputs.version"

        /**
         * Current Gradle version
         */
        public val current: String = "steps.$stepId.outputs.current"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
