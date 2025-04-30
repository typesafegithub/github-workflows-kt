package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

sealed interface Artifact

data class TextArtifact(
    val data: () -> String,
) : Artifact

data class JarArtifact(
    val data: () -> ByteArray,
) : Artifact

fun buildVersionArtifacts(actionCoords: ActionCoords): Map<String, Artifact>? =
    with(actionCoords) {
        val jars = buildJars() ?: return null
        val pom = buildPomFile()
        val module = buildModuleFile()
        return mapOf(
            "$mavenName-$version.jar" to JarArtifact(jars.mainJar),
            "$mavenName-$version.jar.md5" to TextArtifact { jars.mainJar().md5Checksum() },
            "$mavenName-$version.jar.sha1" to TextArtifact { jars.mainJar().sha1Checksum() },
            "$mavenName-$version.jar.sha256" to TextArtifact { jars.mainJar().sha256Checksum() },
            "$mavenName-$version.jar.sha512" to TextArtifact { jars.mainJar().sha512Checksum() },
            "$mavenName-$version-sources.jar" to JarArtifact(jars.sourcesJar),
            "$mavenName-$version-sources.jar.md5" to TextArtifact { jars.sourcesJar().md5Checksum() },
            "$mavenName-$version-sources.jar.sha1" to TextArtifact { jars.sourcesJar().sha1Checksum() },
            "$mavenName-$version-sources.jar.sha256" to TextArtifact { jars.sourcesJar().sha256Checksum() },
            "$mavenName-$version-sources.jar.sha512" to TextArtifact { jars.sourcesJar().sha512Checksum() },
            "$mavenName-$version.pom" to TextArtifact { pom },
            "$mavenName-$version.pom.md5" to TextArtifact { pom.md5Checksum() },
            "$mavenName-$version.pom.sha1" to TextArtifact { pom.sha1Checksum() },
            "$mavenName-$version.pom.sha256" to TextArtifact { pom.sha256Checksum() },
            "$mavenName-$version.pom.sha512" to TextArtifact { pom.sha512Checksum() },
            "$mavenName-$version.module" to TextArtifact { module },
            "$mavenName-$version.module.md5" to TextArtifact { module.md5Checksum() },
            "$mavenName-$version.module.sha1" to TextArtifact { module.sha1Checksum() },
            "$mavenName-$version.module.sha256" to TextArtifact { module.sha256Checksum() },
            "$mavenName-$version.module.sha512" to TextArtifact { module.sha512Checksum() },
        )
    }
