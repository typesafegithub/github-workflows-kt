package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

suspend fun ActionCoords.buildPackageArtifacts(githubToken: String): Map<String, String> {
    val mavenMetadata = buildMavenMetadataFile(githubToken = githubToken)
    return mapOf(
        "maven-metadata.xml" to mavenMetadata,
        "maven-metadata.xml.md5" to mavenMetadata.md5Checksum(),
    )
}
