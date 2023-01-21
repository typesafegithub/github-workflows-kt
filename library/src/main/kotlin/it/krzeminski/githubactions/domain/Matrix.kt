package it.krzeminski.githubactions.domain

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public open class Matrix {
    internal lateinit var job: Job<*, *>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()

    public object EMPTY : Matrix()

    public val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    public fun matrixItem(variableName: String, values: List<Any?>): Ref =
        Ref(variableName = variableName, values = values)

    public inner class Ref(
        public val variableName: String,
        public val values: List<Any?>,
    ) : ReadOnlyProperty<Matrix, String> {
        override fun getValue(thisRef: Matrix, property: KProperty<*>): String {
            return "matrix.$variableName"
        }
    }
}
