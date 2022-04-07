// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.appleboy

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: SCP Command to Transfer Files
 *
 * How to Use SCP Command to Transfer Files/Folders in Linux
 *
 * [Action on GitHub](https://github.com/appleboy/scp-action)
 */
public class ScpActionV0(
    /**
     * scp remote host
     */
    public val host: String? = null,
    /**
     * scp remote port
     */
    public val port: Int? = null,
    /**
     * scp username
     */
    public val username: String? = null,
    /**
     * scp password
     */
    public val password: String? = null,
    /**
     * timeout for ssh to remote host
     */
    public val timeout: String? = null,
    /**
     * timeout for scp command
     */
    public val commandTimeout: String? = null,
    /**
     * content of ssh private key. ex raw content of ~/.ssh/id_rsa
     */
    public val key: String? = null,
    /**
     * path of ssh private key
     */
    public val keyPath: String? = null,
    /**
     * ssh key passphrase
     */
    public val passphrase: String? = null,
    /**
     * fingerprint SHA256 of the host public key, default is to skip verification
     */
    public val fingerprint: String? = null,
    /**
     * include more ciphers with use_insecure_cipher
     */
    public val useInsecureCipher: Boolean? = null,
    /**
     * target path on the server
     */
    public val target: String? = null,
    /**
     * scp file list
     */
    public val source: String? = null,
    /**
     * remove target folder before upload data
     */
    public val rm: Boolean? = null,
    /**
     * enable debug message
     */
    public val debug: Boolean? = null,
    /**
     * remove the specified number of leading path elements
     */
    public val stripComponents: Int? = null,
    /**
     * use `--overwrite` flag with tar
     */
    public val overwrite: Boolean? = null,
    /**
     * temporary path for tar file on the dest host
     */
    public val tarTmpPath: String? = null,
    /**
     * ssh proxy remote host
     */
    public val proxyHost: String? = null,
    /**
     * ssh proxy remote port
     */
    public val proxyPort: Int? = null,
    /**
     * ssh proxy username
     */
    public val proxyUsername: String? = null,
    /**
     * ssh proxy password
     */
    public val proxyPassword: String? = null,
    /**
     * ssh proxy key passphrase
     */
    public val proxyPassphrase: String? = null,
    /**
     * timeout for ssh to proxy host
     */
    public val proxyTimeout: String? = null,
    /**
     * content of ssh proxy private key. ex raw content of ~/.ssh/id_rsa
     */
    public val proxyKey: String? = null,
    /**
     * path of ssh proxy private key
     */
    public val proxyKeyPath: String? = null,
    /**
     * fingerprint SHA256 of the host public key, default is to skip verification
     */
    public val proxyFingerprint: String? = null,
    /**
     * include more ciphers with use_insecure_cipher
     */
    public val proxyUseInsecureCipher: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("appleboy", "scp-action", _customVersion ?: "v0.1.2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            host?.let { "host" to it },
            port?.let { "port" to it.toString() },
            username?.let { "username" to it },
            password?.let { "password" to it },
            timeout?.let { "timeout" to it },
            commandTimeout?.let { "command_timeout" to it },
            key?.let { "key" to it },
            keyPath?.let { "key_path" to it },
            passphrase?.let { "passphrase" to it },
            fingerprint?.let { "fingerprint" to it },
            useInsecureCipher?.let { "use_insecure_cipher" to it.toString() },
            target?.let { "target" to it },
            source?.let { "source" to it },
            rm?.let { "rm" to it.toString() },
            debug?.let { "debug" to it.toString() },
            stripComponents?.let { "strip_components" to it.toString() },
            overwrite?.let { "overwrite" to it.toString() },
            tarTmpPath?.let { "tar_tmp_path" to it },
            proxyHost?.let { "proxy_host" to it },
            proxyPort?.let { "proxy_port" to it.toString() },
            proxyUsername?.let { "proxy_username" to it },
            proxyPassword?.let { "proxy_password" to it },
            proxyPassphrase?.let { "proxy_passphrase" to it },
            proxyTimeout?.let { "proxy_timeout" to it },
            proxyKey?.let { "proxy_key" to it },
            proxyKeyPath?.let { "proxy_key_path" to it },
            proxyFingerprint?.let { "proxy_fingerprint" to it },
            proxyUseInsecureCipher?.let { "proxy_use_insecure_cipher" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
