package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.expressions.expr
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public open class Matrix {
    internal lateinit var job: Job<*, *>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()

    public object EMPTY : Matrix()

    public val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    public fun matrixItem(): Ref = Ref()

    public inner class Ref : ReadWriteProperty<Matrix, String> {
        private var initialized: Boolean = false
        override fun getValue(thisRef: Matrix, property: KProperty<*>): String {
            val key = property.name
            check(initialized) {
                "output '$key' must be initialized"
            }
            return "matrix.$key"
        }

        override fun setValue(thisRef: Matrix, property: KProperty<*>, value: String) {
            val key = property.name
            check(!initialized) {
                "Value for output '$key' can be assigned only once!"
            }
            _outputMapping[key] = expr(value)
            initialized = true
        }
    }
}
