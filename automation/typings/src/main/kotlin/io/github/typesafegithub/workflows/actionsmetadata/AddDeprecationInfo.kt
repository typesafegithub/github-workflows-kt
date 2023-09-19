package io.github.typesafegithub.workflows.actionsmetadata

import io.github.typesafegithub.workflows.actionsmetadata.model.Version
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest

fun List<ActionBindingRequest>.addDeprecationInfo(): List<ActionBindingRequest> =
    this.groupBy { "${it.actionCoords.owner}/${it.actionCoords.name}" }
        .mapValues { (_, requests) ->
            val maxVersion = requests.maxByOrNull { Version(it.actionCoords.version) }?.actionCoords?.version
                ?: error("Coding error: there should be at least one element in the group of actions!")
            setDeprecatedByVersionForNonNewestVersions(requests, maxVersion)
        }
        .toList()
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
