// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

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
 * Action: Setup Python
 *
 * Set up a specific version of Python and add the command-line tools to the PATH.
 *
 * [Action on GitHub](https://github.com/actions/setup-python)
 */
public data class SetupPythonV4 private constructor(
    /**
     * Version range or exact version of Python or PyPy to use, using SemVer's version range syntax.
     * Reads from .python-version if unset.
     */
    public val pythonVersion: String? = null,
    /**
     * File containing the Python version to use. Example: .python-version
     */
    public val pythonVersionFile: String? = null,
    /**
     * Used to specify a package manager for caching in the default directory. Supported values:
     * pip, pipenv, poetry.
     */
    public val cache: SetupPythonV4.PackageManager? = null,
    /**
     * The target architecture (x86, x64) of the Python or PyPy interpreter.
     */
    public val architecture: SetupPythonV4.Architecture? = null,
    /**
     * Set this option if you want the action to check for the latest available version that
     * satisfies the version spec.
     */
    public val checkLatest: Boolean? = null,
    /**
     * The token used to authenticate when fetching Python distributions from
     * https://github.com/actions/python-versions. When running this action on github.com, the default
     * value is sufficient. When running on GHES, you can pass a personal access token for github.com
     * if you are experiencing rate limiting.
     */
    public val token: String? = null,
    /**
     * Used to specify the path to dependency files. Supports wildcards or a list of file names for
     * caching multiple dependencies.
     */
    public val cacheDependencyPath: List<String>? = null,
    /**
     * Set this option if you want the action to update environment variables.
     */
    public val updateEnvironment: Boolean? = null,
    /**
     * When 'true', a version range passed to 'python-version' input will match prerelease versions
     * if no GA versions are found. Only 'x.y' version range is supported for CPython.
     */
    public val allowPrereleases: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupPythonV4.Outputs>("actions", "setup-python", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        pythonVersion: String? = null,
        pythonVersionFile: String? = null,
        cache: SetupPythonV4.PackageManager? = null,
        architecture: SetupPythonV4.Architecture? = null,
        checkLatest: Boolean? = null,
        token: String? = null,
        cacheDependencyPath: List<String>? = null,
        updateEnvironment: Boolean? = null,
        allowPrereleases: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(pythonVersion=pythonVersion, pythonVersionFile=pythonVersionFile, cache=cache,
            architecture=architecture, checkLatest=checkLatest, token=token,
            cacheDependencyPath=cacheDependencyPath, updateEnvironment=updateEnvironment,
            allowPrereleases=allowPrereleases, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            pythonVersion?.let { "python-version" to it },
            pythonVersionFile?.let { "python-version-file" to it },
            cache?.let { "cache" to it.stringValue },
            architecture?.let { "architecture" to it.stringValue },
            checkLatest?.let { "check-latest" to it.toString() },
            token?.let { "token" to it },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString("\n") },
            updateEnvironment?.let { "update-environment" to it.toString() },
            allowPrereleases?.let { "allow-prereleases" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class PackageManager(
        public val stringValue: String,
    ) {
        public object Pip : SetupPythonV4.PackageManager("pip")

        public object Pipenv : SetupPythonV4.PackageManager("pipenv")

        public object Poetry : SetupPythonV4.PackageManager("poetry")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV4.PackageManager(customStringValue)
    }

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X64 : SetupPythonV4.Architecture("x64")

        public object X86 : SetupPythonV4.Architecture("x86")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV4.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The installed Python or PyPy version. Useful when given a version range as input.
         */
        public val pythonVersion: String = "steps.$stepId.outputs.python-version"

        /**
         * A boolean value to indicate a cache entry was found
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"

        /**
         * The absolute path to the Python or PyPy executable.
         */
        public val pythonPath: String = "steps.$stepId.outputs.python-path"
    }
}
