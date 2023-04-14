// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.madhead

import io.github.typesafegithub.workflows.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class CheckGradleVersionV1 private constructor(
    /**
     * Relative path to gradlew executable
     */
    public val gradlew: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<CheckGradleVersionV1.Outputs>("madhead", "check-gradle-version", _customVersion ?: "v1")
        {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        gradlew: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(gradlew=gradlew, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            gradlew?.let { "gradlew" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Project Gradle version
         */
        public val version: String = "steps.$stepId.outputs.version"

        /**
         * Current Gradle version
         */
        public val current: String = "steps.$stepId.outputs.current"
    }
}
