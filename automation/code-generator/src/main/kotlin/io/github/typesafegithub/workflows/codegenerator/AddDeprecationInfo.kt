package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.codegenerator.model.ActionBindingRequest
import io.github.typesafegithub.workflows.shared.internal.model.Version

fun List<ActionBindingRequest>.addDeprecationInfo(): List<ActionBindingRequest> =
    this
        .groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }
        .mapValues { (_, requests) ->
            val maxVersion =
                requests.maxByOrNull { Version(it.actionCoords.version) }?.actionCoords?.version
                    ?: error("Coding error: there should be at least one element in the group of actions!")
            setDeprecatedByVersionForNonNewestVersions(requests, maxVersion)
        }.toList()
        .flatMap { it.second }

private fun setDeprecatedByVersionForNonNewestVersions(
    requests: List<ActionBindingRequest>,
    maxVersion: String,
) = requests.map {
    if (it.actionCoords.version != maxVersion) {
        it.copy(actionCoords = it.actionCoords.copy(deprecatedByVersion = maxVersion))
    } else {
        it
    }
}
