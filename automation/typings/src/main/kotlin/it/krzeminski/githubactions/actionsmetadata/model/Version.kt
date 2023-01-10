package it.krzeminski.githubactions.actionsmetadata.model

data class Version(val version: String) : Comparable<Version> {
    val input: String = version.removePrefix("v").removePrefix("V")
    val major = input.substringBefore(".").toIntOrNull() ?: 0
    val minor = input.substringAfter(".").substringBefore(".").toIntOrNull() ?: 0
    val patch = input.substringAfterLast(".").toIntOrNull() ?: 0

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

    fun isMajorVersion(): Boolean =
        version.contains(".").not()
}
