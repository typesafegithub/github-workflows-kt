package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.ktor.client.HttpClient

suspend fun ActionCoords.buildPackageArtifacts(
    githubToken: String,
    httpClient: HttpClient,
): Map<String, String> {
    val mavenMetadata = buildMavenMetadataFile(owner = owner, name = name, githubToken = githubToken, httpClient)
    return mapOf(
        "maven-metadata.xml" to mavenMetadata,
    )
}
