// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.samkirkland

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

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
    public val port: String? = null,
    /**
     * protocol to deploy with - ftp, ftps, or ftps-legacy
     */
    public val protocol: String? = null,
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
    public val dryRun: String? = null,
    /**
     * Deletes ALL contents of server-dir, even items in excluded with exclude argument
     */
    public val dangerousCleanSlate: String? = null,
    /**
     * An array of glob patterns, these files will not be included in the publish/delete process
     */
    public val exclude: String? = null,
    /**
     * How verbose should the information be - minimal, standard, or verbose
     */
    public val logLevel: String? = null,
    /**
     * strict or loose
     */
    public val security: String? = null
) : Action("SamKirkland", "FTP-Deploy-Action", "v4.3.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "server" to server,
            "username" to username,
            "password" to password,
            port?.let { "port" to it },
            protocol?.let { "protocol" to it },
            localDir?.let { "local-dir" to it },
            serverDir?.let { "server-dir" to it },
            stateName?.let { "state-name" to it },
            dryRun?.let { "dry-run" to it },
            dangerousCleanSlate?.let { "dangerous-clean-slate" to it },
            exclude?.let { "exclude" to it },
            logLevel?.let { "log-level" to it },
            security?.let { "security" to it },
        ).toTypedArray()
    )
}
