package io.github.typesafegithub.workflows.actionbindinggenerator

import java.util.Locale

public fun String.toKotlinPackageName(): String =
    replace("-", "")
        .lowercase()

public fun String.toPascalCase(): String {
    val hasOnlyUppercases = none { it in 'a'..'z' }
    val normalizedString = if (hasOnlyUppercases) lowercase() else this
    return normalizedString.replace("+", "-plus-")
        .split("-", "_", " ", ".", "/")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

public fun String.toCamelCase(): String =
    toPascalCase().replaceFirstChar { it.lowercase() }

public fun String.removeTrailingWhitespacesForEachLine(): String =
    lines().joinToString(separator = "\n") { it.trimEnd() }
