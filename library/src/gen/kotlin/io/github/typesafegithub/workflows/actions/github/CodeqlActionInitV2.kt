// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.github

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
 * Action: CodeQL: Init
 *
 * Set up CodeQL
 *
 * [Action on GitHub](https://github.com/github/codeql-action/tree/v2/init)
 */
public data class CodeqlActionInitV2 private constructor(
    /**
     * URL of CodeQL tools
     */
    public val tools: String? = null,
    /**
     * The languages to be analysed
     */
    public val languages: List<String>? = null,
    /**
     * GitHub token to use for authenticating with this instance of GitHub. To download custom packs
     * from multiple registries, use the registries input.
     */
    public val token: String? = null,
    /**
     * Use this input only when you need to download CodeQL packages from another instance of
     * GitHub. If you only need to download packages from this GitHub instance, use the token input
     * instead.
     *
     * A YAML string that defines the list of GitHub container registries to use for downloading
     * packs. The string is in the following form (the | is required on the first line):
     *
     * registries: |
     *     - url: https://containers.GHEHOSTNAME1/v2/
     *       packages:
     *         - my-company/&#42;
     *         - my-company2/&#42;
     *       token: \$\{{ secrets.GHEHOSTNAME1_TOKEN }}
     *
     *     - url: https://ghcr.io/v2/
     *       packages: &#42;/&#42;
     *       token: \$\{{ secrets.GHCR_TOKEN }}
     *
     * The `url` property contains the URL to the container registry you want to connect to.
     *
     * The `packages` property contains a single glob string or a list of glob strings, specifying
     * which packages should be retrieved from this particular container registry. Order is important.
     * Earlier entries will match before later entries.
     *
     * The `token` property contains a connection token for this registry.    required: false
     */
    public val registries: String? = null,
    public val matrix: String? = null,
    /**
     * Path of the config file to use
     */
    public val configFile: String? = null,
    /**
     * Path where CodeQL databases should be created. If not specified, a temporary directory will
     * be used.
     */
    public val dbLocation: String? = null,
    /**
     * Comma-separated list of additional queries to run. By default, this overrides the same
     * setting in a configuration file; prefix with "+" to use both sets of queries.
     */
    public val queries: List<String>? = null,
    /**
     * [Experimental] Comma-separated list of packs to run. Reference a pack in the format
     * `scope/name[@version]`. If `version` is not specified, then the latest version of the pack is
     * used. By default, this overrides the same setting in a configuration file; prefix with "+" to
     * use both sets of packs.
     * This input is only available in single-language analyses. To use packs in multi-language
     * analyses, you must specify packs in the codeql-config.yml file.
     */
    public val packs: List<String>? = null,
    /**
     * A token for fetching external config files and queries if they reside in a private repository
     * in the same GitHub instance that is running this action.
     */
    public val externalRepositoryToken: String? = null,
    /**
     * Try to auto-install your python dependencies
     */
    public val setupPythonDependencies: Boolean? = null,
    /**
     * Path of the root source code directory, relative to $GITHUB_WORKSPACE.
     */
    public val sourceRoot: String? = null,
    /**
     * The amount of memory in MB that can be used by CodeQL extractors. By default, CodeQL
     * extractors will use most of the memory available in the system (which for GitHub-hosted runners
     * is 6GB for Linux, 5.5GB for Windows, and 13GB for macOS). This input also sets the amount of
     * memory that can later be used by the "analyze" action.
     */
    public val ram: Int? = null,
    /**
     * The number of threads that can be used by CodeQL extractors. By default, CodeQL extractors
     * will use all the hardware threads available in the system (which for GitHub-hosted runners is 2
     * for Linux and Windows and 3 for macOS). This input also sets the number of threads that can
     * later be used by the "analyze" action.
     */
    public val threads: Int? = null,
    /**
     * Enable debugging mode. This will result in more output being produced which may be useful
     * when debugging certain issues. Debugging mode is enabled automatically when step debug logging
     * is turned on.
     */
    public val debug: Boolean? = null,
    /**
     * The name of the artifact to store debugging information in. This is only used when debug mode
     * is enabled.
     */
    public val debugArtifactName: String? = null,
    /**
     * The name of the database uploaded to the debugging artifact. This is only used when debug
     * mode is enabled.
     */
    public val debugDatabaseName: String? = null,
    /**
     * Explicitly enable or disable TRAP caching rather than respecting the feature flag for it.
     */
    public val trapCaching: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CodeqlActionInitV2.Outputs>("github", "codeql-action/init", _customVersion ?:
        "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        tools: String? = null,
        languages: List<String>? = null,
        token: String? = null,
        registries: String? = null,
        matrix: String? = null,
        configFile: String? = null,
        dbLocation: String? = null,
        queries: List<String>? = null,
        packs: List<String>? = null,
        externalRepositoryToken: String? = null,
        setupPythonDependencies: Boolean? = null,
        sourceRoot: String? = null,
        ram: Int? = null,
        threads: Int? = null,
        debug: Boolean? = null,
        debugArtifactName: String? = null,
        debugDatabaseName: String? = null,
        trapCaching: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(tools=tools, languages=languages, token=token, registries=registries, matrix=matrix,
            configFile=configFile, dbLocation=dbLocation, queries=queries, packs=packs,
            externalRepositoryToken=externalRepositoryToken,
            setupPythonDependencies=setupPythonDependencies, sourceRoot=sourceRoot, ram=ram,
            threads=threads, debug=debug, debugArtifactName=debugArtifactName,
            debugDatabaseName=debugDatabaseName, trapCaching=trapCaching,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            tools?.let { "tools" to it },
            languages?.let { "languages" to it.joinToString(",") },
            token?.let { "token" to it },
            registries?.let { "registries" to it },
            matrix?.let { "matrix" to it },
            configFile?.let { "config-file" to it },
            dbLocation?.let { "db-location" to it },
            queries?.let { "queries" to it.joinToString(",") },
            packs?.let { "packs" to it.joinToString(",") },
            externalRepositoryToken?.let { "external-repository-token" to it },
            setupPythonDependencies?.let { "setup-python-dependencies" to it.toString() },
            sourceRoot?.let { "source-root" to it },
            ram?.let { "ram" to it.toString() },
            threads?.let { "threads" to it.toString() },
            debug?.let { "debug" to it.toString() },
            debugArtifactName?.let { "debug-artifact-name" to it },
            debugDatabaseName?.let { "debug-database-name" to it },
            trapCaching?.let { "trap-caching" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The path of the CodeQL binary used for analysis
         */
        public val codeqlPath: String = "steps.$stepId.outputs.codeql-path"
    }
}
