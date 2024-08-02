package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.charleskorn.kaml.Yaml
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.TYPING_CATALOG
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.repoName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchUri
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.myYaml
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.io.IOException
import java.net.URI

internal fun ActionCoords.provideTypes(
    metadataRevision: MetadataRevision,
    fetchUri: (URI) -> String = ::fetchUri,
): Pair<Map<String, Typing>, TypingActualSource?> =
    (
        this.fetchTypingMetadata(metadataRevision, fetchUri)
            ?: this.toMajorVersion().fetchFromTypingsFromCatalog(fetchUri)
    )?.let { Pair(it.first.toTypesMap(), it.second) }
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

private fun ActionCoords.fetchTypingMetadata(
    metadataRevision: MetadataRevision,
    fetchUri: (URI) -> String = ::fetchUri,
): Pair<ActionTypes, TypingActualSource>? {
    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
        }
    val list = listOf(actionTypesYmlUrl(gitRef), actionTypesYamlUrl(gitRef))
    val typesMetadataYaml =
        list.firstNotNullOfOrNull { url ->
            try {
                println("  ... types from $url")
                fetchUri(URI(url))
            } catch (e: IOException) {
                null
            }
        } ?: return null

    return Pair(myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes()), ACTION)
}

private fun ActionCoords.fetchFromTypingsFromCatalog(fetchUri: (URI) -> String = ::fetchUri): Pair<ActionTypes, TypingActualSource>? =
    (
        fetchTypingsFromUrl(url = actionTypesFromCatalog(), fetchUri = fetchUri)
            ?: fetchTypingsForOlderVersionFromCatalog(fetchUri = fetchUri)
    )?.let { Pair(it, TYPING_CATALOG) }

private fun ActionCoords.fetchTypingsForOlderVersionFromCatalog(fetchUri: (URI) -> String): ActionTypes? {
    val metadataUrl = this.catalogMetadata()
    val metadataYml =
        try {
            println("  ... metadata from $metadataUrl")
            fetchUri(URI(metadataUrl))
        } catch (e: IOException) {
            return null
        }
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
    return fetchTypingsFromUrl(url = adjustedCoords.actionTypesFromCatalog(), fetchUri = fetchUri)
}

private fun fetchTypingsFromUrl(
    url: String,
    fetchUri: (URI) -> String,
): ActionTypes? {
    val typesMetadataYml =
        try {
            println("  ... types from $url")
            fetchUri(URI(url))
        } catch (e: IOException) {
            null
        } ?: return null
    return myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYml, ActionTypes())
}

internal fun ActionTypes.toTypesMap(): Map<String, Typing> =
    inputs.mapValues { (key, value) ->
        value.toTyping(key)
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
