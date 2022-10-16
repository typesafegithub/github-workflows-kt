// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.docker

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
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
public class LoginActionV2(
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
    _customVersion: String? = null,
) : Action("docker", "login-action", _customVersion ?: "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            registry?.let { "registry" to it },
            username?.let { "username" to it },
            password?.let { "password" to it },
            ecr?.let { "ecr" to it.toString() },
            logout?.let { "logout" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
