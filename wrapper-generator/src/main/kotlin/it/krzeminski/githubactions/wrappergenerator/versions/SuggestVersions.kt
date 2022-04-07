package it.krzeminski.githubactions.wrappergenerator.versions

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
 * new minor version available: v2.2.0 for ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")
 * ```
 */
fun main() {
    val githubAuthorization = System.getenv("GITHUB_TOKEN")
        ?: error(
            """
            |Missing environment variable export GITHUB_TOKEN=token
            |Create a personal token at https://github.com/settings/tokens
           """.trimMargin()
        )

    val actionsMap = wrappersToGenerate
        // .take(3)
        .map { it.actionCoords }
        .groupBy { "${it.owner}/${it.name}" }
    for (list in actionsMap.values) {
        val coords = list.maxByOrNull { it.version }!!
        val existingVersions = list.map { it.version }
        val available = coords.fetchAvailableVersions(githubAuthorization)
        suggestNewerVersion(existingVersions, available)?.let { message ->
            println("$message for ${coords.prettyPrint}")
        }
    }
}

fun List<GithubTag>.versions(): List<String> =
    this.map { it.ref.substringAfterLast("/") }

fun suggestNewerVersion(existingVersions: List<String>, availableVersions: List<String>): String? {
    val maxExisting = existingVersions.maxByOrNull { VersionComparable(it) }!!

    if (existingVersions.all { isMajorVersion(it) }) {
        val majorVersions = availableVersions
            .map { it.substringBefore(".") }
            .distinct()
            .sortedBy { VersionComparable(it) }
        val newer = majorVersions.filter { VersionComparable(it) > VersionComparable(maxExisting) }
        return "new major version(s) available: $newer".takeIf { newer.isNotEmpty() }
    } else {
        val maxAvailableVersion = availableVersions.maxOrNull()
        return "new minor version available: $maxAvailableVersion"
            .takeIf { maxAvailableVersion != null && VersionComparable(maxAvailableVersion) > VersionComparable(maxExisting) }
    }
}

private fun isMajorVersion(version: String): Boolean =
    version.contains(".").not()
