package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.getOrElse
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.model.Version
import java.time.format.DateTimeFormatter

private val logger = logger { }

internal suspend fun ActionCoords.buildMavenMetadataFile(
    githubAuthToken: String,
    fetchAvailableVersions: suspend (
        owner: String,
        name: String,
        githubAuthToken: String?,
    ) -> Either<String, List<Version>> = ::fetchAvailableVersions,
): String? {
    val availableVersions =
        fetchAvailableVersions(owner, name, githubAuthToken)
            .getOrElse {
                logger.error { it }
                emptyList()
            }.filter { it.isMajorVersion() || (significantVersion < FULL) }
    val newest = availableVersions.maxOrNull() ?: return null
    val lastUpdated =
        DateTimeFormatter
            .ofPattern("yyyyMMddHHmmss")
            .format(newest.getReleaseDate())
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <metadata>
          <groupId>$owner</groupId>
          <artifactId>$mavenName</artifactId>
          <versioning>
            <latest>$newest</latest>
            <release>$newest</release>
            <versions>
${availableVersions.joinToString(separator = "\n") {
        "              <version>$it</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}
