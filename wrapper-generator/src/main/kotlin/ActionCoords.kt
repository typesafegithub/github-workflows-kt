data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
) {
    // TODO improve
    fun ownerPackage(): String {
        val escaped = owner.replace("-", "").lowercase()
        return "it.krzeminski.githubactions.actions.$escaped"
    }

    // TODO improve
    fun className() = name.capitalize().replace("-", "") + "V" + version[1]
}


