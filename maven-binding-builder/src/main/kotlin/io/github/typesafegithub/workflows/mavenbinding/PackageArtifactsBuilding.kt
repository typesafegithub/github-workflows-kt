package io.github.typesafegithub.workflows.mavenbinding

suspend fun ActionCoords.buildPackageArtifacts(githubToken: String): Map<String, String> {
    val mavenMetadata = buildMavenMetadataFile(owner = owner, name = name, githubToken = githubToken)
    return mapOf(
        "maven-metadata.xml" to mavenMetadata,
    )
}
