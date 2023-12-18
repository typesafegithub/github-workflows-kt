package io.github.typesafegithub.workflows.codegenerator.versions

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.prettyPrint
import io.github.typesafegithub.workflows.codegenerator.bindingsToGenerate
import io.github.typesafegithub.workflows.codegenerator.model.Version
import java.io.File

/**
 * Suggest newer versions for the binding generator
 *
 * You need to set an environment variable
 *  `$ export GITHUB_TOKEN=token`
 *
 *  Create your token at https://github.com/settings/tokens
 *
 *  Usage:
 *
 *  ```
 * $ ./gradlew :code-generator:suggestVersions
 * ```
 */
suspend fun main() {
    var output: String = ""
    val githubAuthorization = getGithubToken()

    val actionsMap: Map<ActionCoords, List<Version>> =
        bindingsToGenerate
            .map { it.actionCoords }
            .filter { it.deprecatedByVersion == null }
            .filter { it.isTopLevel }
            .groupBy { ActionCoords(it.owner, it.name, version = "*") }
            .mapKeys { it.key.copy(version = it.value.last().version) }
            .mapValues { (_, value) -> value.map { Version(it.version) } }

    for ((coords, existingVersions) in actionsMap) {
        val available = coords.fetchAvailableVersions(githubAuthorization)
        suggestNewerVersion(existingVersions, available)?.let { message ->
            val outputForAction = "$message for ${coords.prettyPrint}"
            println(outputForAction)
            output += "$outputForAction\n"
        }
    }
    File("build/suggestVersions.md").let { file ->
        if (output.isNotEmpty()) {
            println("Updated ${file.absolutePath}")
            file.parentFile.mkdirs()
            file.writeText(output)
        } else {
            println("No versions to suggest - ensuring that ${file.absolutePath} doesn't exist")
            file.delete()
        }
    }
}

fun List<GithubRef>.versions(): List<Version> =
    this.map { githubRef ->
        val version = githubRef.ref.substringAfterLast("/")
        Version(version)
    }

fun suggestNewerVersion(
    existingVersions: List<Version>,
    availableVersions: List<Version>,
): String? {
    if (availableVersions.isEmpty()) {
        return null
    }

    val maxExisting =
        existingVersions.maxOrNull()
            ?: error("Invalid call existingVersions=$existingVersions availableVersions=$availableVersions")

    val majorVersions =
        availableVersions
            .filter { it.isMajorVersion() }
            .sorted()

    val newerMajorVersions = majorVersions.filter { it > maxExisting }.sorted()
    return "new version(s) available: $newerMajorVersions".takeIf { newerMajorVersions.isNotEmpty() }
}
