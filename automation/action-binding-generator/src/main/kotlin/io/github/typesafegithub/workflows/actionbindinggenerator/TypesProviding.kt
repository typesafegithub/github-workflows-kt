package io.github.typesafegithub.workflows.actionbindinggenerator

import com.charleskorn.kaml.Yaml
import io.github.typesafegithub.workflows.actionbindinggenerator.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.TypingActualSource.CACHE
import io.github.typesafegithub.workflows.actionbindinggenerator.TypingActualSource.TYPING_CATALOG
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Path
import java.time.LocalDate

internal fun ActionCoords.provideTypes(
    metadataRevision: MetadataRevision,
    fetchUri: (URI) -> String = ::fetchUri,
    useCache: Boolean = true,
): Pair<Map<String, Typing>, TypingActualSource?> =
    (
        this.fetchTypingMetadata(metadataRevision, fetchUri, useCache = useCache)
            ?: this.fetchFromTypingsFromCatalog(fetchUri)
    )
        ?.let { Pair(it.first.toTypesMap(), it.second) }
        ?: Pair(emptyMap(), null)

private val actionTypesYamlDir: File = File("build/action-types-yaml")

public fun deleteActionTypesYamlCacheIfObsolete() {
    val today = LocalDate.now().toString()
    val dateTxt = actionTypesYamlDir.resolve("date.txt")
    val cacheUpToDate = dateTxt.canRead() && dateTxt.readText() == today

    if (!cacheUpToDate) {
        actionTypesYamlDir.deleteRecursively()
        actionTypesYamlDir.mkdir()
        dateTxt.writeText(today)
    }
}

private fun ActionCoords.actionTypesYmlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$repoName/$gitRef/$subName/action-types.yml"

private fun ActionCoords.actionTypesFromCatalog() =
    "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
        "main/typings/$owner/$repoName/$version/$subName/action-types.yml"

private fun ActionCoords.actionTypesYamlUrl(gitRef: String) =
    "https://raw.githubusercontent.com/$owner/$repoName/$gitRef/$subName/action-types.yaml"

private fun ActionCoords.fetchTypingMetadata(
    metadataRevision: MetadataRevision,
    fetchUri: (URI) -> String = ::fetchUri,
    useCache: Boolean,
): Pair<ActionTypes, TypingActualSource>? {
    val cacheFile = actionTypesYamlDir.resolve("$owner-${name.replace('/', '_')}-$version.yml")
    if (useCache) {
        if (cacheFile.canRead()) {
            println("  ... types from cache: $cacheFile")
            return Pair(myYaml.decodeFromStringOrDefaultIfEmpty(cacheFile.readText(), ActionTypes()), CACHE)
        }
    }

    val gitRef =
        when (metadataRevision) {
            is CommitHash -> metadataRevision.value
            NewestForVersion -> this.version
            FromLockfile -> getCommitHash(this)
        } ?: return null
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

    if (useCache) {
        cacheFile.parentFile.mkdirs()
        cacheFile.writeText(typesMetadataYaml)
    }

    return Pair(myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes()), ACTION)
}

private fun ActionCoords.fetchFromTypingsFromCatalog(fetchUri: (URI) -> String = ::fetchUri): Pair<ActionTypes, TypingActualSource>? =
    fetchTypingsFromUrl(url = actionTypesFromCatalog(), fetchUri = fetchUri)?.let { Pair(it, TYPING_CATALOG) }

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
