package io.github.typesafegithub.workflows.domain

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public open class JobOutputs {
    internal lateinit var job: Job<*>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()

    public object EMPTY : JobOutputs()

    public val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    public fun <T> output(): Ref<T> = Ref()

    public inner class Ref<T> : ReadWriteProperty<JobOutputs, Expression<T>> {
        private var initialized: Boolean = false

        override fun getValue(
            thisRef: JobOutputs,
            property: KProperty<*>,
        ): Expression<T> {
            val key = property.name
            check(initialized) {
                "output '$key' must be initialized"
            }
            return Expression("needs.${job.id}.outputs.$key")
        }

        override fun setValue(
            thisRef: JobOutputs,
            property: KProperty<*>,
            value: Expression<T>,
        ) {
            val key = property.name
            check(!initialized) {
                "Value for output '$key' can be assigned only once!"
            }
            _outputMapping[key] = value.expressionString
            initialized = true
        }
    }
}
