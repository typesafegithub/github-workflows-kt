data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
) {
    // TODO make unit tests pass
    fun ownerPackage(): String {
        val escaped = owner.replace("-", "").lowercase()
        return "it.krzeminski.githubactions.actions.$escaped"
    }

    // TODO make unit tests pass
    fun className() = name.capitalize().replace("-", "") + "V" + version[1]
}


