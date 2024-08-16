package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

sealed interface Artifact

data class TextArtifact(
    val data: () -> String,
) : Artifact

data class JarArtifact(
    val data: () -> ByteArray,
) : Artifact

fun ActionCoords.buildVersionArtifacts(types: String? = null): Map<String, Artifact>? {
    val jars = buildJars(types = types) ?: return null
    val pom = buildPomFile()
    val module = buildModuleFile()
    return mapOf(
        "$mavenName-$version.jar" to JarArtifact(jars.mainJar),
        "$mavenName-$version.jar.md5" to TextArtifact { jars.mainJar().md5Checksum() },
        "$mavenName-$version-sources.jar" to JarArtifact(jars.sourcesJar),
        "$mavenName-$version-sources.jar.md5" to TextArtifact { jars.sourcesJar().md5Checksum() },
        "$mavenName-$version.pom" to TextArtifact { pom },
        "$mavenName-$version.pom.md5" to TextArtifact { pom.md5Checksum() },
        "$mavenName-$version.module" to TextArtifact { module },
        "$mavenName-$version.module.md5" to TextArtifact { module.md5Checksum() },
    )
}
