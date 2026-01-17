package io.github.typesafegithub.workflows.shared.internal.model

import java.time.ZonedDateTime

data class Version(
    val version: String,
    private val dateProvider: suspend () -> ZonedDateTime? = { null },
    private val commitHashProvider: suspend () -> String? = { null },
) : Comparable<Version> {
    private val versionParts: List<String> = version.removePrefix("v").removePrefix("V").split('.')
    private val versionIntParts: List<Int?> = versionParts.map { it.toIntOrNull() }
    val major: Int = versionIntParts.getOrNull(0) ?: 0
    val minor: Int = versionIntParts.getOrNull(1) ?: 0
    val patch: Int = versionIntParts.getOrNull(2) ?: 0

    @Suppress("ReturnCount")
    override fun compareTo(other: Version): Int {
        versionParts.forEachIndexed { i, part ->
            val otherPart = other.versionParts.getOrNull(i)
            if (otherPart == null) return 1
            val intPart = versionIntParts[i]
            val otherIntPart = other.versionIntParts[i]
            if ((intPart == null) && (otherIntPart == null)) {
                val comparison = part.compareTo(otherPart)
                if (comparison != 0) return comparison
            } else if (intPart == null) {
                return -1
            } else if (otherIntPart == null) {
                return 1
            } else {
                val comparison = intPart.compareTo(otherIntPart)
                if (comparison != 0) return comparison
            }
        }
        if (versionParts.size < other.versionParts.size) return -1
        return version.compareTo(other.version)
    }

    override fun equals(other: Any?): Boolean = this.compareTo(other as Version) == 0

    override fun toString(): String = version

    fun isMajorVersion(): Boolean = versionIntParts.singleOrNull() != null

    fun isFullVersion(): Boolean = versionIntParts.size == 3

    suspend fun getReleaseDate() = dateProvider()

    suspend fun getCommitHash() = commitHashProvider()
}
