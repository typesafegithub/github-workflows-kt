package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.micrometer.core.instrument.MeterRegistry

suspend fun buildPackageArtifacts(
    actionCoords: ActionCoords,
    githubAuthToken: String,
    prefetchBindingArtifacts: (Collection<ActionCoords>) -> Unit,
    meterRegistry: MeterRegistry,
): Map<String, String> {
    val mavenMetadata =
        actionCoords.buildMavenMetadataFile(
            githubAuthToken = githubAuthToken,
            prefetchBindingArtifacts = prefetchBindingArtifacts,
            meterRegistry = meterRegistry,
        ) ?: return emptyMap()
    return mapOf(
        "maven-metadata.xml" to mavenMetadata,
        "maven-metadata.xml.md5" to mavenMetadata.md5Checksum(),
        "maven-metadata.xml.sha1" to mavenMetadata.sha1Checksum(),
        "maven-metadata.xml.sha256" to mavenMetadata.sha256Checksum(),
        "maven-metadata.xml.sha512" to mavenMetadata.sha512Checksum(),
    )
}
