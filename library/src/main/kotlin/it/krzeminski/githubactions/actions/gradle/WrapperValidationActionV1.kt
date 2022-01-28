package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action

class WrapperValidationActionV1(
    val minWrapperCount: Int? = null,
    val allowSnapshots: Boolean? = null,
    val allowChecksums: List<String>? = null,
) : Action("gradle", "wrapper-validation-action", "v1") {
    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            minWrapperCount?.let { "min-wrapper-count" to it.toString() },
            allowSnapshots?.let { "allow-snapshots" to it.toString() },
            allowChecksums?.let { "allow-checksums" to it.joinToString(",") },
        ).toTypedArray()
    )
}
