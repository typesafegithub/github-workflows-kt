package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import kotlinx.serialization.Contextual

public data class Container(
    val image: String,
    val credentials: Credentials? = null,
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val ports: List<String> = emptyList(),
    val volumes: List<String> = emptyList(),
    val options: List<String> = emptyList(),
    override val _customArguments: Map<String, @Contextual Any?> = emptyMap(),
) : HasCustomArguments {
    public companion object {
        public fun healthCheck(
            command: String,
            intervalSeconds: Int = 2,
            timeoutSeconds: Int = 2,
            retries: Int = 30,
        ): List<String> {
            require(intervalSeconds > 0) { "intervalSeconds must be greater than 0" }
            require(timeoutSeconds > 0) { "timeoutSeconds must be greater than 0" }
            require(retries > 0) { "retries must be greater than 0" }

            return listOf(
                "--health-cmd \"$command\"",
                "--health-interval ${intervalSeconds}s",
                "--health-timeout ${timeoutSeconds}s",
                "--health-retries $retries",
            )
        }
    }
}

public data class Credentials(
    val username: String,
    val password: String,
)
