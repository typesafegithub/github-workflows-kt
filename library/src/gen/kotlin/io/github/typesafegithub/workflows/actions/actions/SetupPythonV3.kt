// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Deprecated
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
@Deprecated(
    message = "This action has a newer major version: SetupPythonV4",
    replaceWith = ReplaceWith("SetupPythonV4"),
)
public data class SetupPythonV3 private constructor(
    /**
     * Version range or exact version of a Python version to use, using SemVer's version range
     * syntax.
     */
    public val pythonVersion: String? = null,
    /**
     * Used to specify a package manager for caching in the default directory. Supported values:
     * pip, pipenv, poetry.
     */
    public val cache: SetupPythonV3.PackageManager? = null,
    /**
     * The target architecture (x86, x64) of the Python interpreter.
     */
    public val architecture: SetupPythonV3.Architecture? = null,
    /**
     * Used to pull python distributions from actions/python-versions. Since there's a default, this
     * is typically not supplied by the user.
     */
    public val token: String? = null,
    /**
     * Used to specify the path to dependency files. Supports wildcards or a list of file names for
     * caching multiple dependencies.
     */
    public val cacheDependencyPath: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<SetupPythonV3.Outputs>("actions", "setup-python", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        pythonVersion: String? = null,
        cache: SetupPythonV3.PackageManager? = null,
        architecture: SetupPythonV3.Architecture? = null,
        token: String? = null,
        cacheDependencyPath: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(pythonVersion=pythonVersion, cache=cache, architecture=architecture, token=token,
            cacheDependencyPath=cacheDependencyPath, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            pythonVersion?.let { "python-version" to it },
            cache?.let { "cache" to it.stringValue },
            architecture?.let { "architecture" to it.stringValue },
            token?.let { "token" to it },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class PackageManager(
        public val stringValue: String,
    ) {
        public object Pip : SetupPythonV3.PackageManager("pip")

        public object Pipenv : SetupPythonV3.PackageManager("pipenv")

        public object Poetry : SetupPythonV3.PackageManager("poetry")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV3.PackageManager(customStringValue)
    }

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X64 : SetupPythonV3.Architecture("x64")

        public object X86 : SetupPythonV3.Architecture("x86")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV3.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The installed python version. Useful when given a version range as input.
         */
        public val pythonVersion: String = "steps.$stepId.outputs.python-version"

        /**
         * A boolean value to indicate a cache entry was found
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"
    }
}
