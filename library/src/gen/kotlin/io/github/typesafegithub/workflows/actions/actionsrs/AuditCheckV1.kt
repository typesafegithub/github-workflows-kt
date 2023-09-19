// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actionsrs

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
 * Action: rust-audit-check
 *
 * Run cargo audit and check for security advisories
 *
 * [Action on GitHub](https://github.com/actions-rs/audit-check)
 */
public data class AuditCheckV1 private constructor(
    /**
     * GitHub Actions token
     */
    public val token: String,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions-rs", "audit-check", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        token: String,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(token=token, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "token" to token,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
