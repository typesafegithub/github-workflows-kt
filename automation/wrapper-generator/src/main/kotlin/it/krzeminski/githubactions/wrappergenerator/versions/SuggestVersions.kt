package it.krzeminski.githubactions.wrappergenerator.versions

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate
import java.io.File

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
 * ```
 */
fun main() {
    var output: String = ""
    val githubAuthorization = getGithubToken()

    val actionsMap: Map<ActionCoords, List<Version>> = wrappersToGenerate
        .map { it.actionCoords }
        .filter { it.deprecatedByVersion == null }
        .groupBy { ActionCoords(it.owner, it.name, version = "*") }
        .mapKeys { it.key.copy(version = it.value.last().version) }
        .mapValues { (_, value) -> value.map { Version(it.version) } }

    for ((coords, existingVersions) in actionsMap) {
        val available = coords.fetchAvailableVersions(githubAuthorization)
        suggestNewerVersion(existingVersions, available)?.let { message ->
            output += "$message for ${coords.prettyPrint}\n"
        }
    }
    println(output)
    File("build/suggestVersions.md").let { file ->
        if (output.isNotEmpty()) {
            println("Updated ${file.absolutePath}")
            file.writeText(output)
        } else {
            println("No versions to suggest - ensuring that ${file.absolutePath} doesn't exist")
            file.delete()
        }
    }
}

fun List<GithubRef>.versions(): List<Version> =
    this.map { githubTag ->
        val version = githubTag.ref.substringAfterLast("/")
        Version(version)
    }

fun suggestNewerVersion(existingVersions: List<Version>, availableVersions: List<Version>): String? {
    if (availableVersions.isEmpty()) {
        return null
    }

    val maxExisting = existingVersions.maxOrNull()
        ?: error("Invalid call existingVersions=$existingVersions availableVersions=$availableVersions")

    val majorVersions = availableVersions
        .filter { it.isMajorVersion() }
        .sorted()

    val newerMajorVersions = majorVersions.filter { it > maxExisting }.sorted()
    return "new version(s) available: $newerMajorVersions".takeIf { newerMajorVersions.isNotEmpty() }
}
