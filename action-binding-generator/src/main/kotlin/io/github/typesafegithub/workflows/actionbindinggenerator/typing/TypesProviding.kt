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
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

private val logger = logger { }
private val defaultHttpClient = HttpClient(CIO)

internal suspend fun ActionCoords.provideTypes(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient = defaultHttpClient,
): ActionTypings =
    (
        this.fetchTypingMetadata(metadataRevision, httpClient)
            ?: this.toMajorVersion().fetchFromTypingsFromCatalog(httpClient)
    )?.let { (typings, typingActualSource) ->
        ActionTypings(
            inputTypings = typings.toTypesMap(),
            source = typingActualSource,
            fromFallbackVersion = typings.fromFallbackVersion,
        )
    } ?: ActionTypings()

private fun ActionCoords.actionTypesYmlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action-types.yml"

private fun ActionCoords.actionTypesYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$name/$gitRef$subName/action-types.yaml"

private const val CATALOG_BASE_URL =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/main/typings"

private fun ActionCoords.actionTypesFromCatalog() =
    "$CATALOG_BASE_URL/${owner.lowercase()}/${name.lowercase()}/$version$subName/action-types.yml"

private fun ActionCoords.catalogMetadata() = "$CATALOG_BASE_URL/${owner.lowercase()}/${name.lowercase()}/metadata.yml"

private suspend fun ActionCoords.fetchTypingMetadata(
    metadataRevision: MetadataRevision,
    httpClient: HttpClient,
): Pair<ActionTypes, TypingActualSource>? {
    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
        }
    val list = listOf(actionTypesYmlUrl(gitRef), actionTypesYamlUrl(gitRef))
    val typesMetadataYaml =
        list.firstNotNullOfOrNull { url ->
            logger.info { "  ... types from action $url" }
            val response = httpClient.get(url)
            when (response.status) {
                HttpStatusCode.OK -> response.bodyAsText()
                HttpStatusCode.NotFound -> {
                    logger.info { "  ... types from action were not found: $url" }
                    null
                }
                else -> throw IOException("Failed fetching from $url")
            }
        } ?: return null

    return Pair(yaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes()), ACTION)
}

private suspend fun ActionCoords.fetchFromTypingsFromCatalog(
    httpClient: HttpClient,
): Pair<ActionTypes, TypingActualSource>? =
    (
        fetchTypingsFromUrl(url = this.actionTypesFromCatalog(), httpClient = httpClient)
            ?: this.fetchTypingsForOlderVersionFromCatalog(httpClient = httpClient)
    )?.let { Pair(it, TYPING_CATALOG) }

private suspend fun ActionCoords.fetchTypingsForOlderVersionFromCatalog(httpClient: HttpClient): ActionTypes? {
    val metadataUrl = this.catalogMetadata()
    logger.info { "  ... metadata from $metadataUrl" }
    val response = httpClient.get(metadataUrl)
    val metadataYml =
        when (response.status) {
            HttpStatusCode.OK -> response.bodyAsText()
            HttpStatusCode.NotFound -> return null
            else -> throw IOException("Failed fetching from $metadataUrl")
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
        httpClient = httpClient,
    )?.copy(fromFallbackVersion = true)
}

private suspend fun fetchTypingsFromUrl(
    url: String,
    httpClient: HttpClient,
): ActionTypes? {
    logger.info { "  ... types from catalog $url" }
    val response = httpClient.get(url)
    val typesMetadataYml =
        when (response.status) {
            HttpStatusCode.OK -> response.bodyAsText()
            HttpStatusCode.NotFound -> {
                logger.info { "  ... types from catalog were not found: $url" }
                return null
            }
            else -> throw IOException("Failed fetching from $url")
        }
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
