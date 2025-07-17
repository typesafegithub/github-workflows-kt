package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.charleskorn.kaml.AnchorsAndAliases
import com.charleskorn.kaml.EmptyYamlDocumentException
import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.TYPING_CATALOG
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchUri
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.io.FileNotFoundException
import java.net.URI

private val logger = logger { }

internal fun ActionCoords.provideTypes(
    metadataRevision: MetadataRevision,
    fetchUri: (URI) -> String = ::fetchUri,
): ActionTypings =
    (
        this.fetchTypingMetadata(metadataRevision, fetchUri)
            ?: this.toMajorVersion().fetchFromTypingsFromCatalog(fetchUri)
    )?.let { (typings, typingActualSource) ->
        ActionTypings(
            inputTypings = typings.toTypesMap(),
            source = typingActualSource,
            fromFallbackVersion = typings.fromFallbackVersion,
        )
    } ?: ActionTypings()

private fun ActionCoords.actionTypesYmlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action-types.yml"

private fun ActionCoords.actionTypesFromCatalog() =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
        "main/typings/${owner.lowercase()}/${name.lowercase()}/$version$subName/action-types.yml"

private fun ActionCoords.catalogMetadata() =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
        "main/typings/${owner.lowercase()}/${name.lowercase()}/metadata.yml"

private fun ActionCoords.actionTypesYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action-types.yaml"

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
                logger.info { "  ... types from action $url" }
                fetchUri(URI(url))
            } catch (e: FileNotFoundException) {
                logger.info { "  ... types from action were not found: $url" }
                null
            }
        } ?: return null

    return Pair(yaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes()), ACTION)
}

private fun ActionCoords.fetchFromTypingsFromCatalog(
    fetchUri: (URI) -> String = ::fetchUri,
): Pair<ActionTypes, TypingActualSource>? =
    (
        fetchTypingsFromUrl(url = this.actionTypesFromCatalog(), fetchUri = fetchUri)
            ?: this.fetchTypingsForOlderVersionFromCatalog(fetchUri = fetchUri)
    )?.let { Pair(it, TYPING_CATALOG) }

private fun ActionCoords.fetchTypingsForOlderVersionFromCatalog(fetchUri: (URI) -> String): ActionTypes? {
    val metadataUrl = this.catalogMetadata()
    val metadataYml =
        try {
            logger.info { "  ... metadata from $metadataUrl" }
            fetchUri(URI(metadataUrl))
        } catch (e: FileNotFoundException) {
            return null
        }
    val metadata = yaml.decodeFromString<CatalogMetadata>(metadataYml)
    val requestedVersionAsInt = this.version.versionToIntOrNull() ?: return null
    val fallbackVersion =
        metadata.versionsWithTypings
            .filter { it.versionToInt() < requestedVersionAsInt }
            .maxByOrNull { it.versionToInt() }
            ?: run {
                logger.info { "  ... no fallback version found!" }
                return null
            }
    logger.info { "  ... using fallback version: $fallbackVersion" }
    val adjustedCoords = this.copy(version = fallbackVersion)
    return fetchTypingsFromUrl(
        url = adjustedCoords.actionTypesFromCatalog(),
        fetchUri = fetchUri,
    )?.copy(fromFallbackVersion = true)
}

private fun fetchTypingsFromUrl(
    url: String,
    fetchUri: (URI) -> String,
): ActionTypes? {
    val typesMetadataYml =
        try {
            logger.info { "  ... types from catalog $url" }
            fetchUri(URI(url))
        } catch (e: FileNotFoundException) {
            logger.info { "  ... types from catalog were not found: $url" }
            null
        } ?: return null
    return yaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYml, ActionTypes())
}

internal fun ActionTypes.toTypesMap(): Map<String, Typing> =
    inputs?.mapValues { (key, value) ->
        value.toTyping(key)
    } ?: emptyMap()

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
        runCatching {
            decodeFromString<T>(text)
        }.getOrElse {
            if (it is EmptyYamlDocumentException) {
                return default
            }
            throw it
        }
    } else {
        default
    }

private fun String.versionToInt() = this.versionToIntOrNull() ?: error("Version '$this' cannot be treated as numeric!")

private fun String.versionToIntOrNull() = lowercase().removePrefix("v").toIntOrNull()

private val yaml =
    Yaml(
        configuration =
            Yaml.default.configuration.copy(
                strictMode = false,
                anchorsAndAliases = AnchorsAndAliases.Permitted(),
            ),
    )

@Serializable
private data class CatalogMetadata(
    val versionsWithTypings: List<String>,
)
