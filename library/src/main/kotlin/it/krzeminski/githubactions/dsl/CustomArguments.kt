@file:Suppress("PropertyName", "VariableNaming", "FunctionNaming", "FunctionName")

package it.krzeminski.githubactions.dsl

interface HasCustomArguments {
    val _customArguments: Map<String, CustomValue>
}

@kotlinx.serialization.Serializable
sealed class CustomValue

@kotlinx.serialization.Serializable
data class StringCustomValue(val value: String) : CustomValue()

@kotlinx.serialization.Serializable
data class ListCustomValue(val value: List<String>) : CustomValue()

@kotlinx.serialization.Serializable
data class ObjectCustomValue(val value: Map<String, String>) : CustomValue()

fun IntCustomValue(value: Int): StringCustomValue =
    StringCustomValue("$value")

fun BooleanCustomValue(value: Boolean): StringCustomValue =
    StringCustomValue("$value")

fun ListCustomValue(vararg params: String): ListCustomValue =
    ListCustomValue(params.toList())

fun ListCustomValue(vararg params: Int): ListCustomValue =
    ListCustomValue(params.map { "$it" })
