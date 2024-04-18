package io.github.typesafegithub.workflows.internal.model

public data class Version(val version: String) : Comparable<Version> {
    private val input: String = version.removePrefix("v").removePrefix("V")
    private val major: Int = input.substringBefore(".").toIntOrNull() ?: 0
    private val minor: Int = input.substringAfter(".").substringBefore(".").toIntOrNull() ?: 0
    private val patch: Int = input.substringAfterLast(".").toIntOrNull() ?: 0

    override fun compareTo(other: Version): Int {
        val c1 = major.compareTo(other.major)
        val c2 = minor.compareTo(other.minor)
        val c3 = patch.compareTo(other.patch)
        return when {
            c1 != 0 -> c1
            c2 != 0 -> c2
            c3 != 0 -> c3
            else -> version.compareTo(other.version)
        }
    }

    override fun toString(): String = version

    public fun isMajorVersion(): Boolean = version.contains(".").not()
}
