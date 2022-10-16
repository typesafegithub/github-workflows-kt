// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.entrostat

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Git Secret Reveal Action
 *
 * Reveal the secrets committed into your repo
 *
 * [Action on GitHub](https://github.com/entrostat/git-secret-action)
 */
public class GitSecretActionV3(
    /**
     * The GPG private key to use, you can export this using the command:
     * gpg --armour --export-secret-keys KEY_ID
     */
    public val gpgPrivateKey: String,
    /**
     * The passphrase for the private key, if any
     */
    public val gpgPrivateKeyPassphrase: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("entrostat", "git-secret-action", _customVersion ?: "v3.3.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "gpg-private-key" to gpgPrivateKey,
            gpgPrivateKeyPassphrase?.let { "gpg-private-key-passphrase" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
