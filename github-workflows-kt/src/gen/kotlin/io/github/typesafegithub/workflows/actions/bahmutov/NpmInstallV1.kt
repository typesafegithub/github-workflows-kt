// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.bahmutov

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
 * Action: NPM or Yarn install with caching
 *
 * Install npm dependencies with caching
 *
 * [Action on GitHub](https://github.com/bahmutov/npm-install)
 *
 * @param workingDirectory Working directory to specify subfolder in which dependencies are defined
 * @param useLockFile Option to enable or disable use of a lock file (package-lock.json/yarn.lock)
 * @param useRollingCache Option to enable restoring a cache that doesn't exactly match the
 * lockfile, and expire once a month to keep it from only growing larger
 * @param installCommand Custom install command to use
 * @param cacheKeyPrefix Prefix the cache name with this string
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class NpmInstallV1 private constructor(
    /**
     * Working directory to specify subfolder in which dependencies are defined
     */
    public val workingDirectory: String? = null,
    /**
     * Option to enable or disable use of a lock file (package-lock.json/yarn.lock)
     */
    public val useLockFile: Boolean? = null,
    /**
     * Option to enable restoring a cache that doesn't exactly match the lockfile, and expire once a
     * month to keep it from only growing larger
     */
    public val useRollingCache: Boolean? = null,
    /**
     * Custom install command to use
     */
    public val installCommand: String? = null,
    /**
     * Prefix the cache name with this string
     */
    public val cacheKeyPrefix: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("bahmutov", "npm-install", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        workingDirectory: String? = null,
        useLockFile: Boolean? = null,
        useRollingCache: Boolean? = null,
        installCommand: String? = null,
        cacheKeyPrefix: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(workingDirectory=workingDirectory, useLockFile=useLockFile,
            useRollingCache=useRollingCache, installCommand=installCommand,
            cacheKeyPrefix=cacheKeyPrefix, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            workingDirectory?.let { "working-directory" to it },
            useLockFile?.let { "useLockFile" to it.toString() },
            useRollingCache?.let { "useRollingCache" to it.toString() },
            installCommand?.let { "install-command" to it },
            cacheKeyPrefix?.let { "cache-key-prefix" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
