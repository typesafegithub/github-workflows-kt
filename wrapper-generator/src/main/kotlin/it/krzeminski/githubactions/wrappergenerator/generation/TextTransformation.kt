package it.krzeminski.githubactions.wrappergenerator.generation

import java.util.Locale

fun String.toKotlinPackageName() =
    replace("-", "")

fun String.toPascalCase() =
    split("-")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

fun String.toCamelCase() =
    toPascalCase().replaceFirstChar { it.lowercase() }
