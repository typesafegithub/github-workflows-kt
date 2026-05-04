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
    prefetchBindingArtifacts: (Collection<ActionCoords>) -> Unit = {},
): String? {
    val availableVersions =
        fetchAvailableVersions(actionCoords.owner, actionCoords.name, githubAuthToken, meterRegistry)
            .getOrElse {
                logger.error { it }
                emptyList()
            }.filter { it.isMajorVersion() || (actionCoords.significantVersion < FULL) }
    prefetchBindingArtifacts(availableVersions.map { actionCoords.copy(version = "$it") })
    val commitLenient = actionCoords.significantVersion == COMMIT_LENIENT
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
            <latest>$newest${if (commitLenient) "__${newest.getSha()}" else ""}</latest>
            <release>$newest${if (commitLenient) "__${newest.getSha()}" else ""}</release>
            <versions>
${availableVersions.map { "$it${if (commitLenient) "__${it.getSha()}" else ""}" }.joinToString(separator = "\n") {
        "              <version>$it</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}
