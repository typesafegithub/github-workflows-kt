// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.borales

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: GitHub Action for Yarn
 *
 * Wraps the yarn CLI to enable common yarn commands
 *
 * [Action on GitHub](https://github.com/Borales/actions-yarn)
 *
 * @param cmd Yarn command
 * @param authToken NPM_AUTH_TOKEN
 * @param registryUrl NPM_REGISTRY_URL
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
@Deprecated(
    message = "This action has a newer major version: ActionsYarnV4",
    replaceWith = ReplaceWith("ActionsYarnV4"),
)
public data class ActionsYarnV3 private constructor(
    /**
     * Yarn command
     */
    public val cmd: String,
    /**
     * NPM_AUTH_TOKEN
     */
    public val authToken: String? = null,
    /**
     * NPM_REGISTRY_URL
     */
    public val registryUrl: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("Borales", "actions-yarn", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        cmd: String,
        authToken: String? = null,
        registryUrl: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(cmd=cmd, authToken=authToken, registryUrl=registryUrl, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "cmd" to cmd,
            authToken?.let { "auth-token" to it },
            registryUrl?.let { "registry-url" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
