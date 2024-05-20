package io.github.typesafegithub.workflows.codegenerator.versions

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchMetadata
import io.github.typesafegithub.workflows.codegenerator.bindingsToGenerate
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.getGithubToken
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.ktor.client.HttpClient
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
        val available = fetchAvailableVersions(owner = coords.owner, name = coords.name, githubAuthorization)
        coords.suggestNewerVersion(existingVersions, available)?.let { message ->
            val outputForAction = "# ${coords.prettyPrint}\n$message"
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

suspend fun ActionCoords.suggestNewerVersion(
    existingVersions: List<Version>,
    availableVersions: List<Version>,
    httpClient: HttpClient? = null,
): String? {
    suspend fun ActionCoords.fetchMeta(metadataRevision: MetadataRevision): Metadata =
        this.fetchMetadata(metadataRevision, httpClient) ?: error("Couldn't get metadata for $this!")

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

    val metadata = this.copy(version = maxExisting.version).fetchMeta(FromLockfile)

    val newerMajorVersions =
        majorVersions.filter { it > maxExisting }.sorted()
            .map {
                val thisMetadata = this@suggestNewerVersion.copy(version = it.version).fetchMeta(NewestForVersion)
                val addedInputs = thisMetadata.inputs.keys - metadata.inputs.keys
                val removedInputs = metadata.inputs.keys - thisMetadata.inputs.keys
                val addedOutputs = thisMetadata.outputs.keys - metadata.outputs.keys
                val removedOutputs = metadata.outputs.keys - thisMetadata.outputs.keys
                """
                * $it ([diff](${this.buildGitHubComparisonUrl(maxExisting, it)}))
                  * added inputs: $addedInputs
                  * removed inputs: $removedInputs
                  * added outputs: $addedOutputs
                  * removed outputs: $removedOutputs
                """.trimIndent()
            }
    return newerMajorVersions.joinToString("\n").takeIf { newerMajorVersions.isNotEmpty() }
}

private fun ActionCoords.buildGitHubComparisonUrl(
    version1: Version,
    version2: Version,
): String = "https://github.com/${this.owner}/${this.name}/compare/$version1...$version2#files_bucket"
