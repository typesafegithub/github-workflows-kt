package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.expr
import kotlin.reflect.KProperty


public open class StrategyMatrix(
    private val map: MutableMap<String, List<String>> = mutableMapOf()
): Map<String, List<String>> by map {

    public fun iterate(vararg value: String): Property {
        return Property(value.toList())
    }

    public inner class Property(public val values: List<String>) {
        public operator fun getValue(strategyMatrixContext: StrategyMatrix, property: KProperty<*>): String {
            map[property.name] = values
            return expr("matrix.${property.name}")
        }
    }
}
