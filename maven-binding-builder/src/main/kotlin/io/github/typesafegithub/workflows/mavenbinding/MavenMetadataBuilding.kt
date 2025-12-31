package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.getOrElse
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.micrometer.core.instrument.MeterRegistry
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path

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
        fetchAvailableVersions(this.actionCoords.owner, this.actionCoords.name, githubAuthToken, meterRegistry)
            .getOrElse {
                logger.error { it }
                emptyList()
            }.filter {
                if (this.rawName.endsWith("__commit_lenient")) {
                    it.isFullVersion()
                } else {
                    it.isMajorVersion() || (this.actionCoords.significantVersion < FULL)
                }
            }
    prefetchBindingArtifacts(availableVersions.map { this.actionCoords.copy(version = "$it") })
    val newest = availableVersions.maxOrNull() ?: return null
    val newestMaybeWithCommitHash = if (this.rawName.endsWith("__commit_lenient")) {
        newest.withCommitHash()
    } else {
        newest
    }
    val lastUpdated =
        DateTimeFormatter
            .ofPattern("yyyyMMddHHmmss")
            .format(newest.getReleaseDate())
    val versionsWithCommitHashes = availableVersions.map {
        if (this.rawName.endsWith("__commit_lenient")) {
            it.withCommitHash()
        } else {
            it.version
        }
    }
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <metadata>
          <groupId>${this.actionCoords.owner}</groupId>
          <artifactId>${this.actionCoords.name}</artifactId>
          <versioning>
            <latest>$newestMaybeWithCommitHash</latest>
            <release>$newestMaybeWithCommitHash</release>
            <versions>
${versionsWithCommitHashes.joinToString(separator = "\n") {
        "              <version>$it</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}

private suspend fun Version.withCommitHash(): String =
    "${this.version}__${this.getCommitHash()}"
