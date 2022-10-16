// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.azure

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Azure Container Registry Login
 *
 * Log in to Azure Container Registry (ACR) or any private docker container registry
 *
 * [Action on GitHub](https://github.com/Azure/docker-login)
 */
public class DockerLoginV1(
    /**
     * Container registry username
     */
    public val username: String? = null,
    /**
     * Container registry password
     */
    public val password: String? = null,
    /**
     * Container registry server url
     */
    public val loginServer: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("Azure", "docker-login", _customVersion ?: "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            username?.let { "username" to it },
            password?.let { "password" to it },
            loginServer?.let { "login-server" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
