// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.ruby

import it.krzeminski.githubactions.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup Ruby, JRuby and TruffleRuby
 *
 * Download a prebuilt Ruby and add it to the PATH in 5 seconds
 *
 * [Action on GitHub](https://github.com/ruby/setup-ruby)
 */
public data class SetupRubyV1(
    /**
     * Engine and version to use, see the syntax in the README. Reads from .ruby-version or
     * .tool-versions if unset.
     */
    public val rubyVersion: String? = null,
    /**
     * The version of RubyGems to use. Either 'default' (the default), 'latest', or a version number
     * (e.g., 3.3.5).
     * For 'default', no action is taken and the version of RubyGems that comes with Ruby by default
     * is used.
     * For 'latest', `gem update --system` is run to update to the latest RubyGems version.
     * Similarly, if a version number is given, `gem update --system <version>` is run to update to
     * that version of RubyGems, as long as that version is newer than the one provided by default.
     */
    public val rubygems: String? = null,
    /**
     * The version of Bundler to install. Either 'Gemfile.lock' (the default), 'default', 'latest',
     * 'none', or a version number (e.g., 1, 2, 2.1, 2.1.4).
     * For 'Gemfile.lock', the version of the BUNDLED WITH section from the Gemfile.lock if it
     * exists. If the file or section does not exist then the same as 'default'.
     * For 'default', if the Ruby ships with Bundler 2.2+ as a default gem, that version is used,
     * otherwise the same as 'latest'.
     * For 'latest', the latest compatible Bundler version is installed (Bundler 2 on Ruby >= 2.3,
     * Bundler 1 on Ruby < 2.3).
     * For 'none', nothing is done.
     */
    public val bundler: String? = null,
    /**
     * Run "bundle install", and cache the result automatically. Either true or false.
     */
    public val bundlerCache: Boolean? = null,
    /**
     * The working directory to use for resolving paths for .ruby-version, .tool-versions and
     * Gemfile.lock.
     */
    public val workingDirectory: String? = null,
    /**
     * Arbitrary string that will be added to the cache key of the bundler cache. Set or change it
     * if you need
     * to invalidate the cache.
     */
    public val cacheVersion: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<SetupRubyV1.Outputs>("ruby", "setup-ruby", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            rubyVersion?.let { "ruby-version" to it },
            rubygems?.let { "rubygems" to it },
            bundler?.let { "bundler" to it },
            bundlerCache?.let { "bundler-cache" to it.toString() },
            workingDirectory?.let { "working-directory" to it },
            cacheVersion?.let { "cache-version" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The prefix of the installed ruby
         */
        public val rubyPrefix: String = "steps.$stepId.outputs.ruby-prefix"

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
