package it.krzeminski.githubactions.wrappergenerator.versions

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate

/**
 * Suggest newer versions for the wrapper generator
 *
 * You need to set an environment variable
 *  `$ export GITHUB_TOKEN=token`
 *
 *  Create your token at https://github.com/settings/tokens
 *
 *  Usage:
 *
 *  ```
 * $ ./gradlew :wrapper-generator:suggestVersions
 * new major version(s) available: [v3] for ActionCoords("actions", "cache", "v2")
 * new major version(s) available: [v3] for ActionCoords("actions", "download-artifact", "v2")
 * new version available: v2.2.0 for ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")
 * ```
 */
fun main() {
    val githubAuthorization = System.getenv("GITHUB_TOKEN")
        ?: error(
            """
            |Missing environment variable export GITHUB_TOKEN=token
            |Create a personal token at https://github.com/settings/tokens
            |The token needs to have public_repo scope.
           """.trimMargin()
        )

    val actionsMap: Map<ActionCoords, List<Version>> = wrappersToGenerate
        .map { it.actionCoords }
        .groupBy { ActionCoords(it.owner, it.name, version = "*") }
        .mapValues { (_, value) -> value.map { Version(it.version) } }

    for ((coords, existingVersions) in actionsMap) {
        val available = coords.fetchAvailableVersions(githubAuthorization)
        suggestNewerVersion(existingVersions, available)?.let { message ->
            println("$message for ${coords.prettyPrint}")
        }
    }
}

fun List<GithubTag>.versions(): List<Version> =
    this.map { githubTag ->
        val version = githubTag.ref.substringAfterLast("/")
        Version(version)
    }

fun suggestNewerVersion(existingVersions: List<Version>, availableVersions: List<Version>): String? {
    val maxExisting = existingVersions.maxOrNull()
        ?: error("Invalid call existingVersions=$existingVersions availableVersions=$availableVersions")

    val majorVersions = availableVersions
        .filter { it.isMajorVersion() }
        .sorted()

    val maxAvailableVersion = availableVersions.maxOrNull()
    val coordsUseMajorVersion = existingVersions.all { it.isMajorVersion() }

    return when {
        maxAvailableVersion == null -> null
        coordsUseMajorVersion -> {
            val newerMajorVersions = majorVersions.filter { it > maxExisting }.sorted()
            "new major version(s) available: $newerMajorVersions".takeIf { newerMajorVersions.isNotEmpty() }
        }
        majorVersions.isNotEmpty() -> "major version(s) could be used: $majorVersions"
        else -> "new version available: $maxAvailableVersion".takeIf { maxAvailableVersion > maxExisting }
    }
}
