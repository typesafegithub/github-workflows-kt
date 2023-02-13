// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.domain.actions.ActionWithOutputs
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
public data class SetupPythonV4(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : ActionWithOutputs<SetupPythonV4.Outputs>("actions", "setup-python", _customVersion ?: "v4") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            pythonVersion?.let { "python-version" to it },
            pythonVersionFile?.let { "python-version-file" to it },
            cache?.let { "cache" to it.stringValue },
            architecture?.let { "architecture" to it.stringValue },
            checkLatest?.let { "check-latest" to it.toString() },
            token?.let { "token" to it },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString("\n") },
            updateEnvironment?.let { "update-environment" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

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
        private val stepId: String,
    ) {
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

        public operator fun `get`(outputName: String): String = "steps.$stepId.outputs.$outputName"
    }
}
