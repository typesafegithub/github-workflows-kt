package it.krzeminski.githubactions.dsl

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class JobOutputRef(
    val key: String,
) {
    companion object : ReadOnlyProperty<Any?, JobOutputRef> {
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): JobOutputRef {
            return JobOutputRef(property.name)
        }
    }
}
