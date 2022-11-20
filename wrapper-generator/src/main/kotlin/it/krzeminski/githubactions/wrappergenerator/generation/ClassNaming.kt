package it.krzeminski.githubactions.wrappergenerator.generation

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import java.util.Locale

fun ActionCoords.buildActionClassName(): String {
    val versionString = version
        .let { if (it.startsWith("v")) it else "v$it" }
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
    return "${name.toPascalCase()}$versionString"
}
