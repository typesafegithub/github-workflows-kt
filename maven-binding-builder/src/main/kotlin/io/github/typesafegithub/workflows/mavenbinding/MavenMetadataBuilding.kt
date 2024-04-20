package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal suspend fun buildMavenMetadataFile(
    owner: String,
    name: String,
    githubToken: String,
): String {
    val lastUpdated =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())
    val availableMajorVersions =
        fetchAvailableVersions(owner = owner, name = name.substringBefore("__"), githubToken = githubToken)
            .filter { it.isMajorVersion() }
    val newest = availableMajorVersions.max()
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <metadata>
          <groupId>$owner</groupId>
          <artifactId>${name.replace("__", "/")}</artifactId>
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
