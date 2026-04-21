package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.getOrElse
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.micrometer.core.instrument.MeterRegistry
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
    prefetchBindingArtifacts: (Collection<BindingsServerRequest>) -> Unit = {},
): String? {
    val availableVersions =
        fetchAvailableVersions(actionCoords.owner, actionCoords.name, githubAuthToken, meterRegistry)
            .getOrElse {
                logger.error { it }
                emptyList()
            }.filter { it.isMajorVersion() || (actionCoords.significantVersion < FULL) }
    val commitLenient = actionCoords.significantVersion == COMMIT_LENIENT
    prefetchBindingArtifacts(
        availableVersions.map {
            copy(
                rawVersion = "$it${if (commitLenient) "__${it.sha}" else ""}",
                actionCoords =
                    actionCoords.copy(
                        version = if (commitLenient) it.sha!! else "$it",
                        comment = if (commitLenient) "$it" else null,
                        versionForTypings = "$it",
                    ),
            )
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
            <latest>$newest${if (commitLenient) "__${newest.sha}" else ""}</latest>
            <release>$newest${if (commitLenient) "__${newest.sha}" else ""}</release>
            <versions>
${availableVersions.joinToString(separator = "\n") {
        "              <version>$it${if (commitLenient) "__${it.sha}" else ""}</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}
