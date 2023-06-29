package io.github.typesafegithub.workflows.codegenerator.generation

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import java.util.Locale

fun ActionCoords.buildActionClassName(): String {
    val versionString = version
        .let { if (it.startsWith("v")) it else "v$it" }
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
    return "${name.toPascalCase()}$versionString"
}
