// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.appleboy

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: SSH Remote Commands
 *
 * Executing remote ssh commands
 *
 * [Action on GitHub](https://github.com/appleboy/ssh-action)
 */
public class SshActionV0(
    /**
     * ssh host
     */
    public val host: String? = null,
    /**
     * ssh port
     */
    public val port: Int? = null,
    /**
     * ssh key passphrase
     */
    public val passphrase: String? = null,
    /**
     * ssh username
     */
    public val username: String? = null,
    /**
     * ssh password
     */
    public val password: String? = null,
    /**
     * synchronous execution if multiple hosts
     */
    public val sync: Boolean? = null,
    /**
     * include more ciphers with use_insecure_cipher
     */
    public val useInsecureCipher: Boolean? = null,
    /**
     * the allowed cipher algorithms. If unspecified then a sensible
     */
    public val cipher: String? = null,
    /**
     * timeout for ssh to host
     */
    public val timeout: String? = null,
    /**
     * timeout for ssh command
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
     * sha256 fingerprint of the host public key
     */
    public val fingerprint: String? = null,
    /**
     * ssh proxy host
     */
    public val proxyHost: String? = null,
    /**
     * ssh proxy port
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
     * sha256 fingerprint of the proxy host public key
     */
    public val proxyFingerprint: String? = null,
    /**
     * the allowed cipher algorithms. If unspecified then a sensible
     */
    public val proxyCipher: String? = null,
    /**
     * include more ciphers with use_insecure_cipher
     */
    public val proxyUseInsecureCipher: Boolean? = null,
    /**
     * execute commands
     */
    public val script: String? = null,
    /**
     * stop script after first failure
     */
    public val scriptStop: Boolean? = null,
    /**
     * pass environment variable to shell script
     */
    public val envs: List<String>? = null,
    /**
     * enable debug mode
     */
    public val debug: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("appleboy", "ssh-action", _customVersion ?: "v0.1.5") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            host?.let { "host" to it },
            port?.let { "port" to it.toString() },
            passphrase?.let { "passphrase" to it },
            username?.let { "username" to it },
            password?.let { "password" to it },
            sync?.let { "sync" to it.toString() },
            useInsecureCipher?.let { "use_insecure_cipher" to it.toString() },
            cipher?.let { "cipher" to it },
            timeout?.let { "timeout" to it },
            commandTimeout?.let { "command_timeout" to it },
            key?.let { "key" to it },
            keyPath?.let { "key_path" to it },
            fingerprint?.let { "fingerprint" to it },
            proxyHost?.let { "proxy_host" to it },
            proxyPort?.let { "proxy_port" to it.toString() },
            proxyUsername?.let { "proxy_username" to it },
            proxyPassword?.let { "proxy_password" to it },
            proxyPassphrase?.let { "proxy_passphrase" to it },
            proxyTimeout?.let { "proxy_timeout" to it },
            proxyKey?.let { "proxy_key" to it },
            proxyKeyPath?.let { "proxy_key_path" to it },
            proxyFingerprint?.let { "proxy_fingerprint" to it },
            proxyCipher?.let { "proxy_cipher" to it },
            proxyUseInsecureCipher?.let { "proxy_use_insecure_cipher" to it.toString() },
            script?.let { "script" to it },
            scriptStop?.let { "script_stop" to it.toString() },
            envs?.let { "envs" to it.joinToString(",") },
            debug?.let { "debug" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
