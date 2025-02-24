package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.model.Version
import java.time.format.DateTimeFormatter

internal suspend fun ActionCoords.buildMavenMetadataFile(
    githubToken: String,
    fetchAvailableVersions: suspend (owner: String, name: String, githubToken: String?) -> List<Version> = ::fetchAvailableVersions,
): String? {
    val availableMajorVersions =
        fetchAvailableVersions(owner, name, githubToken)
            .filter { it.isMajorVersion() }
    val newest = availableMajorVersions.maxOrNull() ?: return null
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
${availableMajorVersions.joinToString(separator = "\n") {
        "              <version>$it</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}
