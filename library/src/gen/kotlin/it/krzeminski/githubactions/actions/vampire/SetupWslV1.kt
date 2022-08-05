// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.vampire

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup WSL
 *
 * WSL Setup GitHub Action
 *
 * [Action on GitHub](https://github.com/Vampire/setup-wsl)
 */
public class SetupWslV1(
    /**
     * The WSL distribution to install, update, or configure.
     * Valid values: 'Alpine', 'Debian', 'kali-linux', 'openSUSE-Leap-15.2', 'Ubuntu-22.04',
     * 'Ubuntu-20.04', 'Ubuntu-18.04', 'Ubuntu-16.04'
     */
    public val distribution: SetupWslV1.Distribution? = null,
    /**
     * Whether to use the cache for the downloaded distribution installer.
     * This saves time in subsequent runs, jobs, or workflows but consumes space from
     * the available cache space of the repository.
     * Refer to https://github.com/marketplace/actions/cache for current usage limits.
     */
    public val useCache: Boolean? = null,
    /**
     * The content that will be written to /etc/wsl.conf of the installed distribution.
     * This can be used to adjust various settings as documented at
     * https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configuration-settings-for-wslconf.
     * This can also be used if the distribution is installed already.
     */
    public val wslConf: String? = null,
    /**
     * Whether to set the distribution as default WSL distribution.
     * This can also be used if the distribution is installed already.
     * Default is 'true' if the distribution is going to be installed.
     * Default is 'false' if the distribution is only getting configured, updated, or additional
     * packages installed.
     * The first installed WSL distribution is automatically the default one, independently of this
     * input.
     */
    public val setAsDefault: Boolean? = null,
    /**
     * Whether to update the distribution after installation.
     * This can also be used if the distribution is installed already.
     */
    public val update: Boolean? = null,
    /**
     * Space separated list of additional packages to install after distribution installation.
     * This can also be used if the distribution is installed already.
     */
    public val additionalPackages: List<String>? = null,
    /**
     * The distribution user that should be used to execute run-step scripts with wsl-shell wrapper
     * scripts
     * that are created or updated by the current action invocation. If no value is given, the
     * default user of
     * the distribution at script execution time is used.
     */
    public val wslShellUser: String? = null,
    /**
     * The command that is used in the wsl-shell wrapper scripts to execute the run-step script.
     * The name of the wrapper scripts will be derived from the first word in this input prefixed
     * with 'wsl-'.
     * This means that for the default value, the wrapper scripts will start with 'wsl-bash'.
     * The run-step script file will be given as additional parameter in single quotes after the
     * given string,
     * separated with one space character. The latter point is important, if you need to escape this
     * space character.
     * If the given string contains at least once the sequence '{0}', all occurrences of it will be
     * replaced by the
     * run-step script file without any quotes or anything and it will not be given as additional
     * parameter.
     * This can be used if the script file is needed within the shell command opposed to as
     * additional parameter.
     * This input can also be used if the distribution is installed already to change the wrapper
     * scripts or generate
     * additional ones for other shells.
     */
    public val wslShellCommand: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : ActionWithOutputs<SetupWslV1.Outputs>("Vampire", "setup-wsl", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            distribution?.let { "distribution" to it.stringValue },
            useCache?.let { "use-cache" to it.toString() },
            wslConf?.let { "wsl-conf" to it },
            setAsDefault?.let { "set-as-default" to it.toString() },
            update?.let { "update" to it.toString() },
            additionalPackages?.let { "additional-packages" to it.joinToString(" ") },
            wslShellUser?.let { "wsl-shell-user" to it },
            wslShellCommand?.let { "wsl-shell-command" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public sealed class Distribution(
        public val stringValue: String,
    ) {
        public object Alpine : SetupWslV1.Distribution("Alpine")

        public object Debian : SetupWslV1.Distribution("Debian")

        public object KaliLinux : SetupWslV1.Distribution("kali-linux")

        public object OpenSUSELeap152 : SetupWslV1.Distribution("openSUSE-Leap-15.2")

        public object Ubuntu2204 : SetupWslV1.Distribution("Ubuntu-22.04")

        public object Ubuntu2004 : SetupWslV1.Distribution("Ubuntu-20.04")

        public object Ubuntu1804 : SetupWslV1.Distribution("Ubuntu-18.04")

        public object Ubuntu1604 : SetupWslV1.Distribution("Ubuntu-16.04")

        public class Custom(
            customStringValue: String,
        ) : SetupWslV1.Distribution(customStringValue)
    }

    public class Outputs(
        private val stepId: String,
    ) {
        /**
         * The path to the wsl-shell wrapper that is generated by the current action invocation.
         * Even if the current action invocation does not actually generate the script, because
         * wsl-shell-command is not set explicitly and the script already exists, this output will
         * be set.
         */
        public val wslShellWrapperPath: String = "steps.$stepId.outputs.wsl-shell-wrapper-path"

        /**
         * The path to the distribution-specific wsl-shell wrapper that is generated by the current
         * action invocation.
         * Even if the current action invocation does not actually generate the script, because
         * wsl-shell-command is not set explicitly and the script already exists, this output will
         * be set.
         */
        public val wslShellDistributionWrapperPath: String =
                "steps.$stepId.outputs.wsl-shell-distribution-wrapper-path"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
