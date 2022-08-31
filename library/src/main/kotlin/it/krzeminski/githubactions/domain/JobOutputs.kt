package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.dsl.expressions.expr
import kotlin.properties.ReadOnlyProperty

open class JobOutputs {
    internal lateinit var job: Job<*>
    private val _outputMapping: MutableMap<String, String> = mutableMapOf()
    val outputMapping: Map<String, String> get() = _outputMapping.toMap()

    fun createOutput(key: String): Ref = Ref(key)

    fun createOutput() = ReadOnlyProperty<Any?, Ref> { _, property ->
        createOutput(property.name)
    }

    inner class Ref(
        val key: String
    ) {
        private var initialized: Boolean = false

        val reference: String get() = "needs.${job.id}.outputs.$key"

        override fun toString() = reference

        operator fun plusAssign(reference: String) {
            require(!initialized) {
                "output '$key' must not be initialized"
            }
            _outputMapping[key] = expr(reference)
            initialized = true
        }

        fun <T> setOutput(
            step: WithOutputs<T>,
            block: (T) -> String,
        ) {
            require(!initialized) {
                "output '$key' must not be initialized"
            }
            _outputMapping[key] = expr(block(step.outputs))
            initialized = true
        }
    }
    object EMPTY: JobOutputs()
}
