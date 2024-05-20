package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import kotlinx.serialization.Contextual

/**
 * @see <a href="https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#jobsjob_idcontainer">GitHub - Job container</a>
 * @see <a href="https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#jobsjob_idservices">GitHub - Job services</a>
 */
@Suppress("MaxLineLength")
public data class Container(
    val image: String,
    val credentials: Credentials? = null,
    val env: Map<String, String> = mapOf(),
    val ports: List<PortMapping> = emptyList(),
    val volumes: List<VolumeMapping> = emptyList(),
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

            val escapedCommand = command.replace("\"", "\"'\"'\"")

            return listOf(
                "--health-cmd \"$escapedCommand\"",
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

/**
 * @see io.github.typesafegithub.workflows.dsl.port
 * @see io.github.typesafegithub.workflows.dsl.tcp
 * @see io.github.typesafegithub.workflows.dsl.udp
 */
public data class PortMapping(
    val host: Int,
    val container: Int? = null,
    val protocol: Protocol = Protocol.All,
) {
    init {
        require(host in portRange) { "host port must be in range $portRange" }
        container?.let { value ->
            require(value in portRange) { "container port must be in range $portRange" }
        }
    }

    private companion object {
        private val portRange = 1..UShort.MAX_VALUE.toInt()
    }

    public sealed interface Protocol {
        public data class Custom(val value: String) : Protocol

        public object All : Protocol

        public object TCP : Protocol

        public object UDP : Protocol
    }
}

/**
 * @see io.github.typesafegithub.workflows.dsl.volume
 */
public data class VolumeMapping(
    val source: String? = null,
    val target: String,
    val isReadOnly: Boolean = false,
) {
    init {
        source?.let {
            require(it.isNotBlank()) { "source must not be blank" }
            require(':' !in it) { "source must not contain ':'" }
        }

        require(target.isNotBlank()) { "target must not be blank" }
        require(':' !in target) { "target must not contain ':'" }
    }
}
