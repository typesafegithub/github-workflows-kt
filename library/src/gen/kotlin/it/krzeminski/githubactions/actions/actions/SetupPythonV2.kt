// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: Setup Python
 *
 * Set up a specific version of Python and add the command-line tools to the PATH.
 *
 * [Action on GitHub](https://github.com/actions/setup-python)
 */
public class SetupPythonV2(
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
    public val cacheDependencyPath: List<String>? = null
) : Action("actions", "setup-python", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            pythonVersion?.let { "python-version" to it },
            cache?.let { "cache" to it.stringValue },
            architecture?.let { "architecture" to it.stringValue },
            token?.let { "token" to it },
            cacheDependencyPath?.let { "cache-dependency-path" to it.joinToString(" ") },
        ).toTypedArray()
    )

    public sealed class PackageManager(
        public val stringValue: String
    ) {
        public object Pip : SetupPythonV2.PackageManager("pip")

        public object Pipenv : SetupPythonV2.PackageManager("pipenv")

        public class Custom(
            customStringValue: String
        ) : SetupPythonV2.PackageManager(customStringValue)
    }

    public sealed class Architecture(
        public val stringValue: String
    ) {
        public object X64 : SetupPythonV2.Architecture("x64")

        public object X86 : SetupPythonV2.Architecture("x86")

        public class Custom(
            customStringValue: String
        ) : SetupPythonV2.Architecture(customStringValue)
    }
}
