package it.krzeminski.githubactions.wrappergenerator.types

import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.TypingsSource
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchUri
import it.krzeminski.githubactions.wrappergenerator.metadata.myYaml
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint
import it.krzeminski.githubactions.wrappergenerator.metadata.releasesUrl
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Path
import java.time.LocalDate

fun WrapperRequest.provideTypes(
    fetchUri: (URI) -> String = ::fetchUri,
    getCommitHash: (ActionCoords) -> String = ::getCommitHash,
): Map<String, Typing> =
    when (typingsSource) {
        is TypingsSource.WrapperGenerator -> typingsSource.inputTypings
        TypingsSource.ActionTypes ->
            this@provideTypes.actionCoords
                .fetchTypingMetadata(fetchUri, getCommitHash).toTypesMap()
    }

val actionTypesYamlDir = File("build/action-types-yaml")

fun deleteActionTypesYamlCacheIfObsolete() {
    val today = LocalDate.now().toString()
    val dateTxt = actionTypesYamlDir.resolve("date.txt")
    val cacheUpToDate = dateTxt.canRead() && dateTxt.readText() == today

    if (!cacheUpToDate) {
        actionTypesYamlDir.deleteRecursively()
        actionTypesYamlDir.mkdir()
        dateTxt.writeText(today)
    }
}

private fun ActionCoords.actionTypesYmlUrl(gitRef: String) = "https://raw.githubusercontent.com/$owner/$name/$gitRef/action-types.yml"

private fun ActionCoords.actionTypesYamlUrl(gitRef: String) = "https://raw.githubusercontent.com/$owner/$name/$gitRef/action-types.yaml"

private fun ActionCoords.fetchTypingMetadata(
    fetchUri: (URI) -> String = ::fetchUri,
    getCommitHash: (ActionCoords) -> String = ::getCommitHash,
): ActionTypes {
    val cacheFile = actionTypesYamlDir.resolve("$owner-$name-$version.yml")
    if (cacheFile.canRead()) {
        println("  ... types from cache: $cacheFile")
        return myYaml.decodeFromStringOrDefaultIfEmpty(cacheFile.readText(), ActionTypes())
    }

    val commitHash = getCommitHash(this)
    val list = listOf(actionTypesYmlUrl(commitHash), actionTypesYamlUrl(commitHash))
    val typesMetadataYaml = list.firstNotNullOfOrNull { url ->
        try {
            println("  ... types from $url")
            fetchUri(URI(url))
        } catch (e: IOException) {
            null
        }
    } ?: error("$prettyPrint\n†Can't fetch any of those URLs:\n- ${list.joinToString(separator = "\n- ")}\nCheck release page $releasesUrl")

    cacheFile.parentFile.mkdirs()
    cacheFile.writeText(typesMetadataYaml)
    return myYaml.decodeFromStringOrDefaultIfEmpty(typesMetadataYaml, ActionTypes())
}

internal fun getCommitHash(actionCoords: ActionCoords) =
    Path.of("actions", actionCoords.owner, actionCoords.name, actionCoords.version, "commit-hash.txt")
        .toFile().readText()

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
        ActionTypeEnum.Float -> error("Float typing not supported!")
        ActionTypeEnum.List -> ListOfTypings(
            delimiter = separator,
            typing = listItem?.toTyping(fieldName) ?: error("Lists should have list-item set!"),
        )
        ActionTypeEnum.Enum -> EnumTyping(
            items = allowedValues,
            typeName = name?.toPascalCase() ?: fieldName.toPascalCase(),
        )
    }

private inline fun <reified T> Yaml.decodeFromStringOrDefaultIfEmpty(text: String, default: T): T =
    if (text.isNotBlank()) {
        decodeFromString(text)
    } else {
        default
    }
