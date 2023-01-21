package it.krzeminski.githubactions.domain

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public open class Matrix {
    internal lateinit var job: Job<*, *>
    private val _strategyMatrix: MutableMap<String, Any?> = mutableMapOf()

    public object EMPTY : Matrix()

    public val strategyMatrix: Map<String, Any?> get() = _strategyMatrix.toMap()

    public fun matrixItem(variableName: String, values: List<Any?>): Ref {
        // TODO handle defining variableName multiple times
        _strategyMatrix[variableName] = values
        return Ref(variableName = variableName)
    }

    public inner class Ref(
        public val variableName: String,
    ) : ReadOnlyProperty<Matrix, String> {
        override fun getValue(thisRef: Matrix, property: KProperty<*>): String {
            return "matrix.$variableName"
        }
    }
}
