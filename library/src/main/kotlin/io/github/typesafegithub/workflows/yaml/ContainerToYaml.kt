package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Credentials

internal fun Container.toYaml(): Map<String, Any?> =
    mapOfNotNullValues(
        "image" to image,
        "ports" to ports.ifEmpty { null },
        "volumes" to volumes.ifEmpty { null },
        "env" to env.ifEmpty { null },
        "options" to options.ifEmpty { null }?.joinToString(" "),
        "credentials" to credentials?.toYaml(),
    ) + _customArguments

private fun Credentials.toYaml(): Map<String, Any?> = mapOf(
    "username" to username,
    "password" to password,
)
