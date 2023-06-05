// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.ericcornelissen

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: SVGO action
 *
 * Automatically run SVGO with GitHub Actions
 *
 * [Action on GitHub](https://github.com/ericcornelissen/svgo-action)
 */
public data class SvgoActionV3 private constructor(
    /**
     * The GITHUB_TOKEN secret
     */
    public val repoToken: String? = null,
    /**
     * Run the action in dry mode (i.e. do not write optimized SVGs)
     */
    public val dryRun: Boolean? = null,
    /**
     * A glob of SVGs that should not be optimized
     */
    public val ignore: List<String>? = null,
    /**
     * Fail the Action run even in the event of a recoverable error
     */
    public val strict: Boolean? = null,
    /**
     * The path of the configuration file for SVGO
     */
    public val svgoConfig: String? = null,
    /**
     * The SVGO version to be used (`2`, `3`, or from `project`)
     */
    public val svgoVersion: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SvgoActionV3.Outputs>("ericcornelissen", "svgo-action", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        repoToken: String? = null,
        dryRun: Boolean? = null,
        ignore: List<String>? = null,
        strict: Boolean? = null,
        svgoConfig: String? = null,
        svgoVersion: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(repoToken=repoToken, dryRun=dryRun, ignore=ignore, strict=strict,
            svgoConfig=svgoConfig, svgoVersion=svgoVersion, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            repoToken?.let { "repo-token" to it },
            dryRun?.let { "dry-run" to it.toString() },
            ignore?.let { "ignore" to it.joinToString("\n") },
            strict?.let { "strict" to it.toString() },
            svgoConfig?.let { "svgo-config" to it },
            svgoVersion?.let { "svgo-version" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Boolean indicating if any SVGs were optimized
         */
        public val didOptimize: String = "steps.$stepId.outputs.DID_OPTIMIZE"

        /**
         * The number of SVGs that were optimized
         */
        public val optimizedCount: String = "steps.$stepId.outputs.OPTIMIZED_COUNT"

        /**
         * The number of SVGs that were detected
         */
        public val svgCount: String = "steps.$stepId.outputs.SVG_COUNT"
    }
}
