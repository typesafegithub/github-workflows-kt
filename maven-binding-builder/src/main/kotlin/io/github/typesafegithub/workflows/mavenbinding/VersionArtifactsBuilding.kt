package io.github.typesafegithub.workflows.mavenbinding

sealed interface Artifact

data class TextArtifact(val data: String) : Artifact

data class JarArtifact(val data: ByteArray) : Artifact {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JarArtifact

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}

fun ActionCoords.buildVersionArtifacts(): Map<String, Artifact> =
    mapOf(
        "$name-$version.jar" to JarArtifact(buildJar(owner = owner, name = name, version = version)),
        "$name-$version.pom" to TextArtifact(buildPomFile(owner = owner, name = name, version = version)),
        "$name-$version.module" to TextArtifact(buildModuleFile(owner = owner, name = name, version = version)),
    )
