// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.samkirkland

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: FTP Deploy
 *
 * Automate deploying websites and more with this GitHub action via FTP and FTPS
 *
 * [Action on GitHub](https://github.com/SamKirkland/FTP-Deploy-Action)
 */
public class FTPDeployActionV4(
    /**
     * ftp server
     */
    public val server: String,
    /**
     * ftp username
     */
    public val username: String,
    /**
     * ftp password
     */
    public val password: String,
    /**
     * Server port to connect to (read your web hosts docs)
     */
    public val port: Int? = null,
    /**
     * protocol to deploy with - ftp, ftps, or ftps-legacy
     */
    public val protocol: FTPDeployActionV4.Protocol? = null,
    /**
     * Folder to upload from, must end with trailing slash /
     */
    public val localDir: String? = null,
    /**
     * Path to upload to on the server. Must end with trailing slash /
     */
    public val serverDir: String? = null,
    /**
     * Path and name of the state file - this file is used to track which files have been deployed
     */
    public val stateName: String? = null,
    /**
     * Prints which modifications will be made with current config options, but doesnt actually make
     * any changes
     */
    public val dryRun: Boolean? = null,
    /**
     * Deletes ALL contents of server-dir, even items in excluded with exclude argument
     */
    public val dangerousCleanSlate: Boolean? = null,
    /**
     * An array of glob patterns, these files will not be included in the publish/delete process
     */
    public val exclude: List<String>? = null,
    /**
     * How verbose should the information be - minimal, standard, or verbose
     */
    public val logLevel: FTPDeployActionV4.LogLevel? = null,
    /**
     * strict or loose
     */
    public val security: FTPDeployActionV4.Security? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customArguments: Map<String, String> = mapOf()
) : Action("SamKirkland", "FTP-Deploy-Action", "v4.3.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "server" to server,
            "username" to username,
            "password" to password,
            port?.let { "port" to it.toString() },
            protocol?.let { "protocol" to it.stringValue },
            localDir?.let { "local-dir" to it },
            serverDir?.let { "server-dir" to it },
            stateName?.let { "state-name" to it },
            dryRun?.let { "dry-run" to it.toString() },
            dangerousCleanSlate?.let { "dangerous-clean-slate" to it.toString() },
            exclude?.let { "exclude" to it.joinToString("\n") },
            logLevel?.let { "log-level" to it.stringValue },
            security?.let { "security" to it.stringValue },
            *_customArguments.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class Protocol(
        public val stringValue: String
    ) {
        public object Ftp : FTPDeployActionV4.Protocol("ftp")

        public object Ftps : FTPDeployActionV4.Protocol("ftps")

        public object FtpsLegacy : FTPDeployActionV4.Protocol("ftps-legacy")

        public class Custom(
            customStringValue: String
        ) : FTPDeployActionV4.Protocol(customStringValue)
    }

    public sealed class LogLevel(
        public val stringValue: String
    ) {
        public object Minimal : FTPDeployActionV4.LogLevel("minimal")

        public object Standard : FTPDeployActionV4.LogLevel("standard")

        public object Verbose : FTPDeployActionV4.LogLevel("verbose")

        public class Custom(
            customStringValue: String
        ) : FTPDeployActionV4.LogLevel(customStringValue)
    }

    public sealed class Security(
        public val stringValue: String
    ) {
        public object Strict : FTPDeployActionV4.Security("strict")

        public object Loose : FTPDeployActionV4.Security("loose")

        public class Custom(
            customStringValue: String
        ) : FTPDeployActionV4.Security(customStringValue)
    }
}
