package io.github.typesafegithub.workflows.actionbindinggenerator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Path
import java.time.LocalDate

/**
 * [Metadata syntax for GitHub Actions](https://docs.github.com/en/actions/creating-actions/metadata-syntax-for-github-actions).
 */

@Serializable
public data class Metadata(
    val name: String,
    val description: String,
    val inputs: Map<String, Input> = emptyMap(),
    val outputs: Map<String, Output> = emptyMap(),
)

@Serializable
public data class Input(
    val description: String = "",
    val default: String? = null,
    val required: Boolean? = null,
    val deprecationMessage: String? = null,
)

@Serializable
public data class Output(
    val description: String = "",
)

private fun ActionCoords.actionYmlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/${name.substringBefore(
        '/',
    )}/$gitRef/${if ("/" in name) "${name.substringAfter('/')}/" else ""}action.yml"

private fun ActionCoords.actionYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/${name.substringBefore(
        '/',
    )}/$gitRef/${if ("/" in name) "${name.substringAfter('/')}/" else ""}action.yaml"

internal val ActionCoords.releasesUrl: String get() = "$gitHubUrl/releases"

internal val ActionCoords.gitHubUrl: String get() = "https://github.com/$owner/$name"

internal fun ActionCoords.fetchMetadata(
    commitHash: String = getCommitHashFromFileSystem(),
    useCache: Boolean = true,
    fetchUri: (URI) -> String = ::fetchUri,
): Metadata {
    if (useCache) {
        val cacheFile = actionYamlDir.resolve("$owner-${name.replace('/', '_')}-$version.yml")
        if (cacheFile.canRead()) {
            println("  ... from cache: $cacheFile")
            return myYaml.decodeFromString(cacheFile.readText())
        }
    }

    val list = listOf(actionYmlUrl(commitHash), actionYamlUrl(commitHash))
    val metadataYaml =
        list.firstNotNullOfOrNull { url ->
            try {
                println("  ... from $url")
                fetchUri(URI(url))
            } catch (e: IOException) {
                null
            }
        } ?: error(
            "$prettyPrint\n†Can't fetch any of those URLs:\n- ${list.joinToString(separator = "\n- ")}\n" +
                "Check release page $releasesUrl",
        )

    if (useCache) {
        val cacheFile = actionYamlDir.resolve("$owner-${name.replace('/', '_')}-$version.yml")
        cacheFile.parentFile.mkdirs()
        cacheFile.writeText(metadataYaml)
    }
    return myYaml.decodeFromString(metadataYaml)
}

private fun ActionCoords.getCommitHashFromFileSystem(): String =
    Path.of("actions", owner, name.substringBefore('/'), version, "commit-hash.txt").toFile().readText().trim()

internal fun fetchUri(uri: URI): String = uri.toURL().readText()

private val myYaml =
    Yaml(
        configuration =
            Yaml.default.configuration.copy(
                strictMode = false,
            ),
    )

private val actionYamlDir: File = File("build/action-yaml")

public fun deleteActionYamlCacheIfObsolete() {
    val today = LocalDate.now().toString()
    val dateTxt = actionYamlDir.resolve("date.txt")
    val cacheUpToDate = dateTxt.canRead() && dateTxt.readText() == today

    if (!cacheUpToDate) {
        actionYamlDir.deleteRecursively()
        actionYamlDir.mkdirs()
        dateTxt.writeText(today)
    }
}
