package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.getOrElse
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.format.DateTimeFormatter

private val logger = logger { }

internal suspend fun BindingsServerRequest.buildMavenMetadataFile(
    githubAuthToken: String,
    meterRegistry: MeterRegistry? = null,
    fetchAvailableVersions: suspend (
        owner: String,
        name: String,
        githubAuthToken: String?,
        meterRegistry: MeterRegistry?,
    ) -> Either<String, List<Version>> = ::fetchAvailableVersions,
    prefetchBindingArtifacts: (Collection<BindingsServerRequest>) -> Unit = { },
): String? {
    val availableVersions =
        fetchAvailableVersions(actionCoords.owner, actionCoords.name, githubAuthToken, meterRegistry)
            .getOrElse {
                logger.error { it }
                emptyList()
            }.filter { it.isMajorVersion() || (actionCoords.significantVersion < FULL) }
    val commitLenient = actionCoords.significantVersion == COMMIT_LENIENT
    prefetchBindingArtifacts(
        availableVersions.flatMap {
            flow {
                emit(V1 to "")
                BindingVersion.entries.forEach { emit(it to "binding_version_${it}___") }
            }.map { (bindingVersion, versionPrefix) ->
                copy(
                    rawVersion = "$versionPrefix$it${if (commitLenient) "__${it.getSha()}" else ""}",
                    bindingVersion = bindingVersion,
                    actionCoords =
                        actionCoords.copy(
                            version = if (commitLenient) it.getSha()!! else "$it",
                            comment = if (commitLenient) "$it" else null,
                            versionForTypings = "$it",
                        ),
                )
            }.toList()
        },
    )
    val newest = availableVersions.maxOrNull() ?: return null
    val lastUpdated =
        DateTimeFormatter
            .ofPattern("yyyyMMddHHmmss")
            .format(newest.getReleaseDate())
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <metadata>
          <groupId>${actionCoords.owner}</groupId>
          <artifactId>$rawName</artifactId>
          <versioning>
            <latest>binding_version_${BindingVersion.entries.last()}___$newest${if (commitLenient) "__${newest.getSha()}" else ""}</latest>
            <release>binding_version_${BindingVersion.entries.last()}___$newest${if (commitLenient) "__${newest.getSha()}" else ""}</release>
            <versions>
${availableVersions.map {
        flow {
            emit("")
            BindingVersion.entries.forEach { emit("binding_version_${it}___") }
        }.map { versionPrefix ->
            "              <version>$versionPrefix$it${if (commitLenient) "__${it.getSha()}" else ""}</version>"
        }.toList()
            .joinToString(separator = "\n")
    }.joinToString(separator = "\n")}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}
