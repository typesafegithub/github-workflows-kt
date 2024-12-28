package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1

sealed interface Artifact

data class TextArtifact(
    val data: () -> String,
) : Artifact

data class JarArtifact(
    val data: () -> ByteArray,
) : Artifact

fun ActionCoords.buildVersionArtifacts(bindingVersion: BindingVersion = V1): Map<String, Artifact>? {
    val jars = buildJars(bindingVersion = bindingVersion) ?: return null
    val pom = buildPomFile(libraryVersion = bindingVersion.libraryVersion)
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
