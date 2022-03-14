@file:Suppress("PropertyName", "VariableNaming", "FunctionNaming", "FunctionName")

package it.krzeminski.githubactions.dsl

typealias CustomArguments = Map<String, CustomValue>

interface HasCustomArguments {
    val _customArguments: CustomArguments
}

sealed class CustomValue

data class StringCustomValue(val value: String) : CustomValue()

data class ListCustomValue(val value: List<String>) : CustomValue()

data class ObjectCustomValue(val value: Map<String, String>) : CustomValue()

fun IntCustomValue(value: Int): StringCustomValue =
    StringCustomValue("$value")

fun BooleanCustomValue(value: Boolean): StringCustomValue =
    StringCustomValue("$value")

fun ListCustomValue(vararg params: String): ListCustomValue =
    ListCustomValue(params.toList())

fun ListCustomValue(vararg params: Int): ListCustomValue =
    ListCustomValue(params.map { "$it" })
