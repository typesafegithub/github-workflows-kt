package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.expressions.expr
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class JobOutputs {
    internal lateinit var job: Job<*>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()

    object EMPTY : JobOutputs()

    val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    fun createOutput(key: String): Ref = Ref(key)

    fun createOutput() = Ref(null)

    inner class Ref(
        key: String? = null
    ) : ReadWriteProperty<JobOutputs, String> {
        var key: String? = key
            private set
        private var initialized: Boolean = false
        override fun getValue(thisRef: JobOutputs, property: KProperty<*>): String {
            val key = key ?: property.name
            require(initialized) {
                "output '$key' must be initialized"
            }
            return "needs.${job.id}.outputs.$key"
        }

        override fun setValue(thisRef: JobOutputs, property: KProperty<*>, value: String) {
            val key = key ?: property.name
            require(!initialized) {
                "output '$key' must not be initialized"
            }
            _outputMapping[key] = expr(value)
            initialized = true
        }
    }
}
