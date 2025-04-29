package io.github.typesafegithub.workflows.actionbindinggenerator.utils

import java.util.Locale

private val normalizationSplitRegex = """[^\p{javaJavaIdentifierStart}\p{javaJavaIdentifierPart}&&[^_]]++""".toRegex()

internal fun String.toPascalCase(): String {
    val hasOnlyUppercases = none { it in 'a'..'z' }
    val normalizedString = if (hasOnlyUppercases) lowercase() else this
    return normalizedString
        .replace("+", "-plus-")
        .split(normalizationSplitRegex)
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

internal fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercase() }

internal fun String.toKotlinPackageName(): String =
    replace("-", "")
        .lowercase()

internal fun String.removeTrailingWhitespacesForEachLine() = lines().joinToString(separator = "\n") { it.trimEnd() }
