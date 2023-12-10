// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
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
    message = "This action has a newer major version: SetupPythonV5",
    replaceWith = ReplaceWith("SetupPythonV5"),
)
public data class SetupPythonV2 private constructor(
    /**
     * Version range or exact version of a Python version to use, using SemVer's version range
     * syntax.
     */
    public val pythonVersion: String? = null,
    /**
     * Used to specify a package manager for caching in the default directory. Supported values:
     * pip, pipenv.
     */
    public val cache: SetupPythonV2.PackageManager? = null,
    /**
     * The target architecture (x86, x64) of the Python interpreter.
     */
    public val architecture: SetupPythonV2.Architecture? = null,
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<SetupPythonV2.Outputs>("actions", "setup-python", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        pythonVersion: String? = null,
        cache: SetupPythonV2.PackageManager? = null,
        architecture: SetupPythonV2.Architecture? = null,
        token: String? = null,
        cacheDependencyPath: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(pythonVersion=pythonVersion, cache=cache, architecture=architecture, token=token,
            cacheDependencyPath=cacheDependencyPath, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            pythonVersion?.let { "python-version" to it },
            cache?.let { "cache" to it.stringValue },
            architecture?.let { "architecture" to it.stringValue },
            token?.let { "token" to it },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class PackageManager(
        public val stringValue: String,
    ) {
        public object Pip : SetupPythonV2.PackageManager("pip")

        public object Pipenv : SetupPythonV2.PackageManager("pipenv")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV2.PackageManager(customStringValue)
    }

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X64 : SetupPythonV2.Architecture("x64")

        public object X86 : SetupPythonV2.Architecture("x86")

        public class Custom(
            customStringValue: String,
        ) : SetupPythonV2.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The installed python version. Useful when given a version range as input.
         */
        public val pythonVersion: String = "steps.$stepId.outputs.python-version"
    }
}
