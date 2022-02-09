import java.net.URI

data class ActionCoords(
    val owner: String,
    val name: String,
    val version: String,
) {

    val manifestUri = URI("https://raw.githubusercontent.com/$owner/$name/$version/action.yml") // TODO what if .yAml?

    fun ownerPackage(): String {
        val escaped = owner.split("-", "_", ".").joinToString(separator = "").lowercase()
        return "it.krzeminski.githubactions.actions.$escaped"
    }

    fun className(): String {
        val majorVersion = version.removePrefix("v").removePrefix("V").substringBefore(".")
        val name = name.split("-", "_", ".").joinToString(separator = "") { part ->
            part.capitalize()
        }
        return "${name}V$majorVersion"
    }
}


