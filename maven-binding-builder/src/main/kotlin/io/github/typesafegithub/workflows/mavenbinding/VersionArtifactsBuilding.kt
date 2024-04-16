package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.ktor.client.HttpClient
import java.security.MessageDigest

sealed interface Artifact

data class TextArtifact(val data: String) : Artifact

data class JarArtifact(val data: ByteArray) : Artifact

fun ActionCoords.buildVersionArtifacts(httpClient: HttpClient): Map<String, Artifact>? {
    val jar = buildJar(owner = owner, name = name.replace("__", "/"), version = version, httpClient) ?: return null
    val pom = buildPomFile(owner = owner, name = name.replace("__", "/"), version = version)
    val module = buildModuleFile(owner = owner, name = name.replace("__", "/"), version = version)
    return mapOf(
        "$name-$version.jar" to JarArtifact(jar),
        "$name-$version.jar.md5" to TextArtifact(jar.md5Checksum()),
        "$name-$version.pom" to TextArtifact(pom),
        "$name-$version.pom.md5" to TextArtifact(pom.md5Checksum()),
        "$name-$version.module" to TextArtifact(module),
        "$name-$version.module.md5" to TextArtifact(module.md5Checksum()),
    )
}

private fun ByteArray.md5Checksum(): String {
    val md5 = MessageDigest.getInstance("MD5")
    val hashBytes = md5.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

private fun String.md5Checksum(): String = this.toByteArray(charset = Charsets.UTF_8).md5Checksum()
