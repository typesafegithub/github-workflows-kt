package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

fun CodeBlock(operation: (CodeBlock.Builder) -> Unit): CodeBlock =
    CodeBlock.builder().apply { operation(this) }.build()

inline fun <reified T : Enum<T>> enumMemberName(input: T): MemberName? =
    enumMemberName<T>(input.name)

inline fun <reified T : Enum<T>> enumMemberName(input: String): MemberName? {
    return enumValues<T>()
        .firstOrNull { it.name == input.toPascalCase() }
        ?.let { MemberName(T::class.asClassName(), it.name) }
        ?: run {
            println("WARNING: Unexpected enum ${T::class} $input=$input expected=${enumValues<T>()}")
            null
        }
}

fun generatePropertyWithLinkedMap(
    property: String,
    map: LinkedHashMap<String, String?>?
) = CodeBlock { builder ->
    if (map == null || map.all { it.value.isNullOrBlank() }) return@CodeBlock

    builder
        .add("%L = linkedMapOf(\n", property)
        .indent()

    for ((key, value) in map) {
        if (value != null) {
            val (template, arg) = value.orExpression()
            builder.add("%S to $template,\n", key, arg)
        }
    }
    builder.unindent()
        .add("),\n")
}
