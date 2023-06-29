// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.docker

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Docker Login
 *
 * GitHub Action to login against a Docker registry
 *
 * [Action on GitHub](https://github.com/docker/login-action)
 */
@Deprecated(
    message = "This action has a newer major version: LoginActionV2",
    replaceWith = ReplaceWith("LoginActionV2"),
)
public data class LoginActionV1 private constructor(
    /**
     * Server address of Docker registry. If not set then will default to Docker Hub
     */
    public val registry: String? = null,
    /**
     * Username used to log against the Docker registry
     */
    public val username: String? = null,
    /**
     * Password or personal access token used to log against the Docker registry
     */
    public val password: String? = null,
    /**
     * Specifies whether the given registry is ECR (auto, true or false)
     */
    public val ecr: Boolean? = null,
    /**
     * Log out from the Docker registry at the end of a job
     */
    public val logout: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("docker", "login-action", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        registry: String? = null,
        username: String? = null,
        password: String? = null,
        ecr: Boolean? = null,
        logout: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(registry=registry, username=username, password=password, ecr=ecr, logout=logout,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            registry?.let { "registry" to it },
            username?.let { "username" to it },
            password?.let { "password" to it },
            ecr?.let { "ecr" to it.toString() },
            logout?.let { "logout" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
