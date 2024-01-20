// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.entrostat

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Git Secret Reveal Action
 *
 * Reveal the secrets committed into your repo
 *
 * [Action on GitHub](https://github.com/entrostat/git-secret-action)
 *
 * @param gpgPrivateKey The GPG private key to use, you can export this using the command:
 * gpg --armour --export-secret-keys KEY_ID
 * @param gpgPrivateKeyPassphrase The passphrase for the private key, if any
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class GitSecretActionV4 private constructor(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("entrostat", "git-secret-action", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        gpgPrivateKey: String,
        gpgPrivateKeyPassphrase: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(gpgPrivateKey=gpgPrivateKey, gpgPrivateKeyPassphrase=gpgPrivateKeyPassphrase,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "gpg-private-key" to gpgPrivateKey,
            gpgPrivateKeyPassphrase?.let { "gpg-private-key-passphrase" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
