package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.dsl.expressions.expr
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public open class JobOutputs {
    internal lateinit var job: Job<*>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()

    public object EMPTY : JobOutputs()

    public val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    public fun output(): Ref = Ref()

    public inner class Ref : ReadWriteProperty<JobOutputs, String> {
        private var initialized: Boolean = false
        override fun getValue(thisRef: JobOutputs, property: KProperty<*>): String {
            val key = property.name
            check(initialized) {
                "output '$key' must be initialized"
            }
            return "needs.${job.id}.outputs.$key"
        }

        override fun setValue(thisRef: JobOutputs, property: KProperty<*>, value: String) {
            val key = property.name
            check(!initialized) {
                "Value for output '$key' can be assigned only once!"
            }
            _outputMapping[key] = expr(value)
            initialized = true
        }
    }
}
