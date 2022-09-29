// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: GitHub Script
 *
 * Run simple scripts using the GitHub client
 *
 * [Action on GitHub](https://github.com/actions/github-script)
 */
public class GithubScriptV6(
    /**
     * The script to run
     */
    public val script: String,
    /**
     * The GitHub token used to create an authenticated client
     */
    public val githubToken: String? = null,
    /**
     * Whether to tell the GitHub client to log details of its requests
     */
    public val debug: Boolean? = null,
    /**
     * An optional user-agent string
     */
    public val userAgent: String? = null,
    /**
     * A comma-separated list of API previews to accept
     */
    public val previews: List<String>? = null,
    /**
     * Either "string" or "json" (default "json")—how the result will be encoded
     */
    public val resultEncoding: GithubScriptV6.Encoding? = null,
    /**
     * The number of times to retry a request
     */
    public val retries: Int? = null,
    /**
     * A comma separated list of status codes that will NOT be retried e.g. "400,500". No effect
     * unless `retries` is set
     */
    public val retryExemptStatusCodes: List<Int>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<GithubScriptV6.Outputs>("actions", "github-script", _customVersion ?: "v6") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "script" to script,
            githubToken?.let { "github-token" to it },
            debug?.let { "debug" to it.toString() },
            userAgent?.let { "user-agent" to it },
            previews?.let { "previews" to it.joinToString(",") },
            resultEncoding?.let { "result-encoding" to it.stringValue },
            retries?.let { "retries" to it.toString() },
            retryExemptStatusCodes?.let { "retry-exempt-status-codes" to it.joinToString(",") {
                    it.toString() } },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public sealed class Encoding(
        public val stringValue: kotlin.String,
    ) {
        public object String : GithubScriptV6.Encoding("string")

        public object Json : GithubScriptV6.Encoding("json")

        public class Custom(
            customStringValue: kotlin.String,
        ) : GithubScriptV6.Encoding(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The return value of the script, stringified with `JSON.stringify`
         */
        public val result: String = "steps.$stepId.outputs.result"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
