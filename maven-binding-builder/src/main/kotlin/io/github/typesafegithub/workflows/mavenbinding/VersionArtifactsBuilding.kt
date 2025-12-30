package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.ktor.client.HttpClient

sealed interface Artifact

data class TextArtifact(
    val data: () -> String,
) : Artifact

data class JarArtifact(
    val data: () -> ByteArray,
) : Artifact

data class VersionArtifacts(
    val files: Map<String, Artifact>,
    val typingActualSource: TypingActualSource?,
)

suspend fun buildVersionArtifacts(
    actionCoords: ActionCoords,
    httpClient: HttpClient,
): VersionArtifacts? {
    with(actionCoords) {
        val jars = buildJars(httpClient = httpClient) ?: return null
        val pom = buildPomFile()
        val mainJarSize by lazy { jars.mainJar().size }
        val mainJarMd5Checksum by lazy { jars.mainJar().md5Checksum() }
        val mainJarSha1Checksum by lazy { jars.mainJar().sha1Checksum() }
        val mainJarSha256Checksum by lazy { jars.mainJar().sha256Checksum() }
        val mainJarSha512Checksum by lazy { jars.mainJar().sha512Checksum() }
        val sourcesJarSize by lazy { jars.sourcesJar().size }
        val sourcesJarMd5Checksum by lazy { jars.sourcesJar().md5Checksum() }
        val sourcesJarSha1Checksum by lazy { jars.sourcesJar().sha1Checksum() }
        val sourcesJarSha256Checksum by lazy { jars.sourcesJar().sha256Checksum() }
        val sourcesJarSha512Checksum by lazy { jars.sourcesJar().sha512Checksum() }
        val module by lazy {
            buildModuleFile(
                mainJarSize,
                mainJarMd5Checksum,
                mainJarSha1Checksum,
                mainJarSha256Checksum,
                mainJarSha512Checksum,
                sourcesJarSize,
                sourcesJarMd5Checksum,
                sourcesJarSha1Checksum,
                sourcesJarSha256Checksum,
                sourcesJarSha512Checksum,
            )
        }
        return VersionArtifacts(
            files =
                mapOf(
                    // Next step: fix the logic so that here we return artifacts
                    // under actually requested name and version. Right now, for commit_lenient,
                    // some parts are omitted, and the server returns 404.
                    "$rawName-$rawVersion.jar" to JarArtifact(jars.mainJar),
                    "$rawName-$rawVersion.jar.md5" to TextArtifact { mainJarMd5Checksum },
                    "$rawName-$rawVersion.jar.sha1" to TextArtifact { mainJarSha1Checksum },
                    "$rawName-$rawVersion.jar.sha256" to TextArtifact { mainJarSha256Checksum },
                    "$rawName-$rawVersion.jar.sha512" to TextArtifact { mainJarSha512Checksum },
                    "$rawName-$rawVersion-sources.jar" to JarArtifact(jars.sourcesJar),
                    "$rawName-$rawVersion-sources.jar.md5" to TextArtifact { sourcesJarMd5Checksum },
                    "$rawName-$rawVersion-sources.jar.sha1" to TextArtifact { sourcesJarSha1Checksum },
                    "$rawName-$rawVersion-sources.jar.sha256" to TextArtifact { sourcesJarSha256Checksum },
                    "$rawName-$rawVersion-sources.jar.sha512" to TextArtifact { sourcesJarSha512Checksum },
                    "$rawName-$rawVersion.pom" to TextArtifact { pom },
                    "$rawName-$rawVersion.pom.md5" to TextArtifact { pom.md5Checksum() },
                    "$rawName-$rawVersion.pom.sha1" to TextArtifact { pom.sha1Checksum() },
                    "$rawName-$rawVersion.pom.sha256" to TextArtifact { pom.sha256Checksum() },
                    "$rawName-$rawVersion.pom.sha512" to TextArtifact { pom.sha512Checksum() },
                    "$rawName-$rawVersion.module" to TextArtifact { module },
                    "$rawName-$rawVersion.module.md5" to TextArtifact { module.md5Checksum() },
                    "$rawName-$rawVersion.module.sha1" to TextArtifact { module.sha1Checksum() },
                    "$rawName-$rawVersion.module.sha256" to TextArtifact { module.sha256Checksum() },
                    "$rawName-$rawVersion.module.sha512" to TextArtifact { module.sha512Checksum() },
                ),
            typingActualSource = jars.typingActualSource,
        )
    }
}
