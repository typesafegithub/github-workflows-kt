package it.krzeminski.githubactions.dsl.expressions

open class ExpressionContext(
    private val path: String,
    val propertyToExprPath: Map<String, String> = MapFromLambda { propertyName -> "$path.$propertyName" }
)

private class MapFromLambda<T>(val operation: (String) -> T) : Map<String, T> by emptyMap() {
    override fun containsKey(key: String) = true
    override fun get(key: String): T = operation(key)
    override fun getOrDefault(key: String, defaultValue: T): T = get(key)
}
