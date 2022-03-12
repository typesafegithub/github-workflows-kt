package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

val CodeBlock.Companion.EMPTY: CodeBlock
    get() = of("")

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

public fun <K : Any, V : Any?> Map<K, V>.joinToCodeBlock(
    separator: CodeBlock = CodeBlock.of(",\n"),
    prefix: CodeBlock = CodeBlock.of("("),
    postfix: CodeBlock = CodeBlock.of(")"),
    ifEmpty: CodeBlock = CodeBlock.EMPTY,
    newLineAtEnd: Boolean = true,
    transform: ((K, V) -> CodeBlock?),
): CodeBlock =
    entries
        .filterNot { (key, value) ->
            value == null || (value is List<*> && value.isEmpty())
        }
        .joinToCodeBlock(separator, prefix, postfix, ifEmpty, newLineAtEnd) { (key, value) ->
        transform(key, value)
    }

fun <T : Any> Iterable<T>.joinToCodeBlock(
    separator: CodeBlock = CodeBlock.of(",\n"),
    prefix: CodeBlock = CodeBlock.of("("),
    postfix: CodeBlock = CodeBlock.of(")"),
    ifEmpty: CodeBlock = CodeBlock.EMPTY,
    newLineAtEnd: Boolean = true,
    transform: ((T) -> CodeBlock?) = { any ->
        templateOf(any)?.let { CodeBlock.of(it, any) }
    },
): CodeBlock {
    val builder = CodeBlock.builder()

    val list = this.toList()
    if (list.isEmpty()) {
        return ifEmpty

    }

    builder.add(prefix)
    val withNewLine = separator.toString().contains("\n")
    if (withNewLine) {
        if (prefix.toString().endsWith("\n").not()) {
            builder.add("\n")
        }
        builder.indent()
    }
    list.forEachIndexed { index, elem ->
        val codeblock = transform(elem) ?: return@forEachIndexed
        builder.add(codeblock)
        if (index != list.lastIndex || withNewLine) {
            if (codeblock.toString().endsWith(separator.toString()).not()) {
                builder.add(separator)
            }
        }
    }
    if (withNewLine) {
        builder.unindent()
    }
    builder.add(postfix)
    if (newLineAtEnd) builder.add("\n")
    return builder.build()
}

fun templateOf(any: Any?): String? = when (any) {
    is Int, is Long, is Short, is Boolean -> "%L"
    is Char -> "'%L'"
    is String -> "%S"
    else -> null
}
