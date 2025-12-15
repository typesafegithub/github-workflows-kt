package io.github.typesafegithub.workflows.actionbindinggenerator.metadata

import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

private val logger = logger { }

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
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action.yml"

private fun ActionCoords.actionYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action.yaml"

public suspend fun ActionCoords.fetchMetadata(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient,
): Metadata? {
    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
        }
    val list = listOf(actionYmlUrl(gitRef), actionYamlUrl(gitRef))

    return list
        .firstNotNullOfOrNull { url ->
            logger.info { "  ... from $url" }
            val response = httpClient.get(url)
            when (response.status) {
                HttpStatusCode.OK -> response.bodyAsText()
                HttpStatusCode.NotFound -> null
                else -> throw IOException("Failed fetching from $url")
            }
        }?.let { yaml.decodeFromString(it) }
}

private val yaml =
    Yaml(
        configuration =
            Yaml.default.configuration.copy(
                strictMode = false,
            ),
    )
