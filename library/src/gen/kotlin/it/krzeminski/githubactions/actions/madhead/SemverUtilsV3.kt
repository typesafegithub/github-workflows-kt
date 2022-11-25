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
 * Action: semver-utils
 *
 * One-stop shop for working with semantic versions in your workflows
 *
 * [Action on GitHub](https://github.com/madhead/semver-utils)
 */
public class SemverUtilsV3(
    /**
     * A version to process
     */
    public val version: String,
    /**
     * A version to compare with, if any
     */
    public val compareTo: String? = null,
    /**
     * A version to diff with. If not specified, the version will be diffed with the `compare-to`
     * input. If `compare-to` is not specified either, nothing happens.
     */
    public val diffWith: String? = null,
    /**
     * A range to check against
     */
    public val satisfies: String? = null,
    /**
     * An identifier to pass to the semver's inc function
     */
    public val identifier: String? = null,
    /**
     * Do not fail on incorrect input
     */
    public val lenient: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SemverUtilsV3.Outputs>("madhead", "semver-utils", _customVersion ?: "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "version" to version,
            compareTo?.let { "compare-to" to it },
            diffWith?.let { "diff-with" to it },
            satisfies?.let { "satisfies" to it },
            identifier?.let { "identifier" to it },
            lenient?.let { "lenient" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * Version's release (major.minor.patch)
         */
        public val release: String = "steps.$stepId.outputs.release"

        /**
         * Version's major number
         */
        public val major: String = "steps.$stepId.outputs.major"

        /**
         * Version's minor number
         */
        public val minor: String = "steps.$stepId.outputs.minor"

        /**
         * Version's patch number
         */
        public val patch: String = "steps.$stepId.outputs.patch"

        /**
         * Version's build
         */
        public val build: String = "steps.$stepId.outputs.build"

        /**
         * Number of components in version's build. Individual сomponents are returned as `build-N`
         * outputs, where an is an index from zero to `build-parts` - 1.
         */
        public val buildParts: String = "steps.$stepId.outputs.build-parts"

        /**
         * Version's pre-release
         */
        public val prerelease: String = "steps.$stepId.outputs.prerelease"

        /**
         * Number of components in version's pre-release. Individual сomponents are returned as
         * `prerelease-N` outputs, where an is an index from zero to `prerelease-parts` - 1.
         */
        public val prereleaseParts: String = "steps.$stepId.outputs.prerelease-parts"

        /**
         * If the compare-to was provided, this output will contain "<" if comes after the version,
         * ">" if it preceeds it, and "=" if they are equal
         */
        public val comparisonResult: String = "steps.$stepId.outputs.comparison-result"

        /**
         * If the diff-to or compare-to were provided, this output will contain the diff result
         */
        public val diffResult: String = "steps.$stepId.outputs.diff-result"

        /**
         * true if the version satisfies the given range
         */
        public val satisfies: String = "steps.$stepId.outputs.satisfies"

        /**
         * A result of the call of the semver's `inc` function with `major` increment
         */
        public val incMajor: String = "steps.$stepId.outputs.inc-major"

        /**
         * A result of the call of the semver's `inc` function with `premajor` increment
         */
        public val incPremajor: String = "steps.$stepId.outputs.inc-premajor"

        /**
         * A result of the call of the semver's `inc` function with `minor` increment
         */
        public val incMinor: String = "steps.$stepId.outputs.inc-minor"

        /**
         * A result of the call of the semver's `inc` function with `preminor` increment
         */
        public val incPreminor: String = "steps.$stepId.outputs.inc-preminor"

        /**
         * A result of the call of the semver's `inc` function with `patch` increment
         */
        public val incPatch: String = "steps.$stepId.outputs.inc-patch"

        /**
         * A result of the call of the semver's `inc` function with `prepatch` increment
         */
        public val incPrepatch: String = "steps.$stepId.outputs.inc-prepatch"

        /**
         * A result of the call of the semver's `inc` function with `prerelease` increment
         */
        public val incPrerelease: String = "steps.$stepId.outputs.inc-prerelease"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
