package io.github.typesafegithub.workflows.actionbindinggenerator

import java.util.Locale

internal fun String.toPascalCase(): String {
    val hasOnlyUppercases = none { it in 'a'..'z' }
    val normalizedString = if (hasOnlyUppercases) lowercase() else this
    return normalizedString.replace("+", "-plus-")
        .split("-", "_", " ", ".", "/")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

internal fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercase() }
