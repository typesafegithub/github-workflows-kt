// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.ruby

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class SetupRubyV1 private constructor(
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
     * For 'latest', `gem update --system` is run to update to the latest compatible RubyGems
     * version.
     * Ruby head/master builds will not be updated.
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
     * Consider the runner as a self-hosted runner, which means not using prebuilt Ruby binaries
     * which only work
     * on GitHub-hosted runners or self-hosted runners with a very similar image to the ones used by
     * GitHub runners.
     * The default is to detect this automatically based on the OS, OS version and architecture.
     */
    public val selfHosted: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupRubyV1.Outputs>("ruby", "setup-ruby", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        rubyVersion: String? = null,
        rubygems: String? = null,
        bundler: String? = null,
        bundlerCache: Boolean? = null,
        workingDirectory: String? = null,
        cacheVersion: String? = null,
        selfHosted: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(rubyVersion=rubyVersion, rubygems=rubygems, bundler=bundler, bundlerCache=bundlerCache,
            workingDirectory=workingDirectory, cacheVersion=cacheVersion, selfHosted=selfHosted,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            rubyVersion?.let { "ruby-version" to it },
            rubygems?.let { "rubygems" to it },
            bundler?.let { "bundler" to it },
            bundlerCache?.let { "bundler-cache" to it.toString() },
            workingDirectory?.let { "working-directory" to it },
            cacheVersion?.let { "cache-version" to it },
            selfHosted?.let { "self-hosted" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The prefix of the installed ruby
         */
        public val rubyPrefix: String = "steps.$stepId.outputs.ruby-prefix"
    }
}
