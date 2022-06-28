package it.krzeminski.githubactions.dsl.expressions

@Suppress("ConstructorParameterNaming")
open class ExpressionContext(
    internal val _path: String,
    val propertyToExprPath: Map<String, String> = MapFromLambda { propertyName -> "$_path.$propertyName" }
) : Map<String, String> by propertyToExprPath

internal class MapFromLambda<T>(val operation: (String) -> T) : Map<String, T> by emptyMap() {
    override fun containsKey(key: String) = true
    override fun get(key: String): T = operation(key)
    override fun getOrDefault(key: String, defaultValue: T): T = get(key)
}
