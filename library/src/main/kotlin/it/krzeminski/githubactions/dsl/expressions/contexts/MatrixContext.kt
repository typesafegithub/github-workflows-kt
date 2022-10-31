package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.expr
import kotlin.reflect.KProperty


public open class StrategyMatrix {
    public val stringsMap: MutableMap<String, List<String>> = mutableMapOf()
    public val objectsMap: MutableMap<String, List<Map<String, String>>> = mutableMapOf()

    // TODO: include, exclude https://github.com/krzema12/github-workflows-kt/issues/297

    public fun strings(vararg value: String): Property {
        return Property(value.toList())
    }

    public fun integers(vararg value: Int): Property {
        return Property(value.map { it.toString() })
    }

    public fun objects(vararg value: Map<String, String>): Property {
        return Property(emptyList(), value.toList())
    }

    public inner class Property(public val strings: List<String>, public val objects: List<Map<String, String>> = emptyList()) {
        public operator fun getValue(strategyMatrixContext: StrategyMatrix, property: KProperty<*>): String {
            if (strings.isNotEmpty()) {
                stringsMap[property.name] = strings
            }
            if (objects.isNotEmpty()) {
                objectsMap[property.name] = objects
            }
            return expr("matrix.${property.name}")
        }
    }
}
