package io.github.typesafegithub.workflows.actionbindinggenerator.metadata

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.myYaml
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path

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

public suspend fun ActionCoords.fetchMetadata(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient? = null,
): Metadata? {
    val thisHttpClient = httpClient ?: myHttpClient
    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
            FromLockfile -> this.getCommitHashFromFileSystem()
        }
    val list = listOf(actionYmlUrl(gitRef), actionYamlUrl(gitRef))

    return list.firstNotNullOfOrNull { url ->
        println("  ... from $url")
        thisHttpClient.get(url)
            .takeIf { it.status != HttpStatusCode.NotFound }
            ?.bodyAsText()
    }?.let { myYaml.decodeFromString(it) }
}

private fun ActionCoords.getCommitHashFromFileSystem(): String =
    Path.of("actions", owner, name.substringBefore('/'), version, "commit-hash.txt").toFile().readText().trim()

private val myHttpClient = HttpClient {}
