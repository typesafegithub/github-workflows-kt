package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Credentials
import io.github.typesafegithub.workflows.domain.PortMapping
import io.github.typesafegithub.workflows.domain.VolumeMapping

internal fun Container.toYaml(): Map<String, Any?> =
    mapOfNotNullValues(
        "image" to image,
        "ports" to ports.ifEmpty { null }?.map(PortMapping::toYaml),
        "volumes" to volumes.ifEmpty { null }?.map(VolumeMapping::toYaml),
        "env" to env.ifEmpty { null },
        "options" to options.ifEmpty { null }?.joinToString(" "),
        "credentials" to credentials?.toYaml(),
    ) + _customArguments

private fun Credentials.toYaml(): Map<String, Any?> = mapOf(
    "username" to username,
    "password" to password,
)

internal fun VolumeMapping.toYaml(): String = buildString {
    if (source != null) {
        append(source)
        append(':')
    }

    append(target)

    if (isReadOnly) append(":ro")
}

internal fun PortMapping.toYaml(): String = buildString {
    append(host)

    if (container != null) {
        append(':')
        append(container)
    }

    val protocol = protocol.toYaml()
    if (protocol.isNotEmpty()) {
        append('/')
        append(protocol)
    }
}

private fun PortMapping.Protocol.toYaml(): String = when (this) {
    is PortMapping.Protocol.Custom -> value
    PortMapping.Protocol.All -> ""
    PortMapping.Protocol.TCP -> "tcp"
    PortMapping.Protocol.UDP -> "udp"
}
