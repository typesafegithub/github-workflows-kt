package it.krzeminski.githubactions.dsl

interface HasFreeYamlArgs {
    val freeYamlArgs: FreeYamlArgs
}

typealias FreeYamlArgs = MutableList<FreeArg>

sealed class FreeArg
data class StringFreeArg(val key: String, val value: String) : FreeArg()
data class ListFreeArg(val key: String, val value: List<String>) : FreeArg()

fun <T : HasFreeYamlArgs> T.withFreeArgs(vararg pairs: Pair<String, Any>): T = apply {
    val map = pairs.mapNotNull { (key, value) ->
        when {
            value is String -> StringFreeArg(key, value)
            value is Boolean -> StringFreeArg(key, value.toString())
            value is Int -> StringFreeArg(key, value.toString())
            value is List<*> -> ListFreeArg(key, value.map { it.toString() })
            else -> null
        }
    }
    require(map.size == pairs.size) { "Invalid free args: must be String or List\nGot ${pairs.toList()}" }
    freeYamlArgs.addAll(map)
}
