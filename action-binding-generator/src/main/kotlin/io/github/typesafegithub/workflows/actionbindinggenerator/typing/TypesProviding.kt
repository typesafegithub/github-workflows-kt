package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.charleskorn.kaml.Yaml
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.TYPING_CATALOG
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.repoName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.myYaml
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path

internal suspend fun ActionCoords.provideTypes(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient = HttpClient {},
): Pair<Map<String, Typing>, TypingActualSource?> =
    (
        this.fetchTypingMetadata(metadataRevision, httpClient)
            ?: this.toMajorVersion().fetchFromTypingsFromCatalog(httpClient)
    )
        ?.let { Pair(it.first.toTypesMap(), it.second) }
        ?: Pair(emptyMap(), null)

private fun ActionCoords.actionTypesYmlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$repoName/$gitRef/$subName/action-types.yml"

private fun ActionCoords.actionTypesFromCatalog() =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
        "main/typings/$owner/$repoName/$version/$subName/action-types.yml"

private fun ActionCoords.catalogMetadata() =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
        "main/typings/$owner/$repoName/metadata.yml"

private fun ActionCoords.actionTypesYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$repoName/$gitRef/$subName/action-types.yaml"

private suspend fun ActionCoords.fetchTypingMetadata(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient = HttpClient {},
): Pair<ActionTypes, TypingActualSource>? {
    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
            FromLockfile -> getCommitHash(this)
        } ?: return null
    val list = listOf(actionTypesYmlUrl(gitRef), actionTypesYamlUrl(gitRef))
    val typesMetadataYaml =
        list.firstNotNullOfOrNull { url ->
            println("  ... types from $url")
            httpClient.get(url)
                .takeIf { it.status != HttpStatusCode.NotFound }
                ?.bodyAsText()
        } ?: return null

    return Pair(myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes()), ACTION)
}

private suspend fun ActionCoords.fetchFromTypingsFromCatalog(
    httpClient: HttpClient =
        HttpClient {
        },
): Pair<ActionTypes, TypingActualSource>? =
    (
        fetchTypingsFromUrl(url = actionTypesFromCatalog(), httpClient)
            ?: fetchTypingsForOlderVersionFromCatalog(httpClient)
    )
        ?.let { Pair(it, TYPING_CATALOG) }

private suspend fun ActionCoords.fetchTypingsForOlderVersionFromCatalog(httpClient: HttpClient): ActionTypes? {
    val metadataUrl = this.catalogMetadata()
    println("  ... metadata from $metadataUrl")
    val metadataYml =
        httpClient.get(metadataUrl)
            .takeIf { it.status != HttpStatusCode.NotFound }
            ?.bodyAsText()
            ?: return null
    val metadata = myYaml.decodeFromString<CatalogMetadata>(metadataYml)
    val fallbackVersion =
        metadata.versionsWithTypings
            .filter { it.versionToInt() < this.version.versionToInt() }
            .maxByOrNull { it.versionToInt() }
            ?: run {
                println("  ... no fallback version found!")
                return null
            }
    println("  ... using fallback version: $fallbackVersion")
    val adjustedCoords = this.copy(version = fallbackVersion)
    return fetchTypingsFromUrl(url = adjustedCoords.actionTypesFromCatalog(), httpClient)
}

private suspend fun fetchTypingsFromUrl(
    url: String,
    httpClient: HttpClient,
): ActionTypes? {
    println("  ... types from $url")
    val typesMetadataYml =
        httpClient.get(url)
            .takeIf { it.status != HttpStatusCode.NotFound }
            ?.bodyAsText()
            ?: return null
    return myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYml, ActionTypes())
}

internal fun getCommitHash(actionCoords: ActionCoords): String? =
    Path.of("actions", actionCoords.owner, actionCoords.name, actionCoords.version, "commit-hash.txt")
        .toFile().let {
            if (it.exists()) it.readText().trim() else null
        }

internal fun ActionTypes.toTypesMap(): Map<String, Typing> {
    return inputs.mapValues { (key, value) ->
        value.toTyping(key)
    }
}

private fun ActionCoords.toMajorVersion(): ActionCoords = this.copy(version = this.version.substringBefore("."))

private fun ActionType.toTyping(fieldName: String): Typing =
    when (this.type) {
        ActionTypeEnum.String -> StringTyping
        ActionTypeEnum.Boolean -> BooleanTyping
        ActionTypeEnum.Integer -> {
            if (this.namedValues.isEmpty()) {
                IntegerTyping
            } else {
                IntegerWithSpecialValueTyping(
                    typeName = name?.toPascalCase() ?: fieldName.toPascalCase(),
                    this.namedValues.mapKeys { (key, _) -> key.toPascalCase() },
                )
            }
        }
        ActionTypeEnum.Float -> FloatTyping
        ActionTypeEnum.List ->
            ListOfTypings(
                delimiter = separator,
                typing = listItem?.toTyping(fieldName) ?: error("Lists should have list-item set!"),
            )
        ActionTypeEnum.Enum ->
            EnumTyping(
                items = allowedValues,
                typeName = name?.toPascalCase() ?: fieldName.toPascalCase(),
            )
    }

private inline fun <reified T> Yaml.decodeFromStringOrDefaultIfEmpty(
    text: String,
    default: T,
): T =
    if (text.isNotBlank()) {
        decodeFromString(text)
    } else {
        default
    }

private fun String.versionToInt() = lowercase().removePrefix("v").toInt()

@Serializable
private data class CatalogMetadata(
    val versionsWithTypings: List<String>,
)
