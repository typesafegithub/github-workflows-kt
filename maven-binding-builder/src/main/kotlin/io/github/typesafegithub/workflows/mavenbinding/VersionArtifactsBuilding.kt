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
    val jars = buildJars(owner = owner, name = name.replace("__", "/"), version = version, bindingVersion = bindingVersion) ?: return null
    val pom = buildPomFile(owner = owner, name = name.replace("__", "/"), version = version, libraryVersion = bindingVersion.libraryVersion)
    val module = buildModuleFile(owner = owner, name = name.replace("__", "/"), version = version)
    return mapOf(
        "$name-$version.jar" to JarArtifact(jars.mainJar),
        "$name-$version.jar.md5" to TextArtifact { jars.mainJar().md5Checksum() },
        "$name-$version-sources.jar" to JarArtifact(jars.sourcesJar),
        "$name-$version-sources.jar.md5" to TextArtifact { jars.sourcesJar().md5Checksum() },
        "$name-$version.pom" to TextArtifact { pom },
        "$name-$version.pom.md5" to TextArtifact { pom.md5Checksum() },
        "$name-$version.module" to TextArtifact { module },
        "$name-$version.module.md5" to TextArtifact { module.md5Checksum() },
    )
}
