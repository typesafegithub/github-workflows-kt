package it.krzeminski.githubactions.wrappergenerator.generation

import java.util.Locale

fun String.toKotlinPackageName() =
    replace("-", "")
        .lowercase()

fun String.toPascalCase() =
    replace("+", "-plus-")
        .split("-", "_")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

fun String.toCamelCase() =
    toPascalCase().replaceFirstChar { it.lowercase() }
