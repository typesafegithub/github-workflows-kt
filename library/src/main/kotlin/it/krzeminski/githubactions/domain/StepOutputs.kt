package it.krzeminski.githubactions.domain

import kotlin.properties.ReadOnlyProperty

open class StepOutputs {
    internal lateinit var stepId: String
    fun property(key: String) = lazy { "steps.$stepId.outputs.$key" }
    fun property(): ReadOnlyProperty<StepOutputs, String> = ReadOnlyProperty { _, property ->
        "steps.$stepId.outputs.${property.name}"
    }
}
