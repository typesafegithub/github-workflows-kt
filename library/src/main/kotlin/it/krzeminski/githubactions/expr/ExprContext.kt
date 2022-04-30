package it.krzeminski.githubactions.expr

@Suppress("ConstructorParameterNaming")
open class ExprContext(
    val _path: String,
    val map: Map<String, String> = MapFromLambda { key -> "$_path.$key" }
)

internal class MapFromLambda<T>(val operation: (String) -> T) :
    Map<String, T> by emptyMap() {
    override fun containsKey(key: String) = true
    override fun get(key: String): T = operation(key)
    override fun getOrDefault(key: String, defaultValue: T): T = get(key)
}
