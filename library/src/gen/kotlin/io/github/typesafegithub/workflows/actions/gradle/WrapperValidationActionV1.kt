// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.gradle

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class WrapperValidationActionV1 private constructor(
    /**
     * Minimum number expected gradle-wrapper.jar files found in the repository. Non-negative
     * number. Higher number is useful in monorepos where each project might have their own wrapper.
     */
    public val minWrapperCount: Int? = null,
    /**
     * Allow Gradle snapshot versions during checksum verification. Boolean, true or false.
     */
    public val allowSnapshots: Boolean? = null,
    /**
     * Accept arbitrary user-defined checksums as valid. Comma separated list of SHA256 checksums
     * (lowercase hex).
     */
    public val allowChecksums: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("gradle", "wrapper-validation-action", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        minWrapperCount: Int? = null,
        allowSnapshots: Boolean? = null,
        allowChecksums: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(minWrapperCount=minWrapperCount, allowSnapshots=allowSnapshots,
            allowChecksums=allowChecksums, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            minWrapperCount?.let { "min-wrapper-count" to it.toString() },
            allowSnapshots?.let { "allow-snapshots" to it.toString() },
            allowChecksums?.let { "allow-checksums" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
