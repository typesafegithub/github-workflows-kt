package io.github.typesafegithub.workflows.actionbindinggenerator

internal fun String.toKotlinPackageName(): String =
    replace("-", "")
        .lowercase()

internal fun String.removeTrailingWhitespacesForEachLine() =
    lines().joinToString(separator = "\n") { it.trimEnd() }
