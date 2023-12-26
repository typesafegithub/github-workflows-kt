package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase
import java.util.Locale

internal fun ActionCoords.buildActionClassName(includeVersion: Boolean = true): String {
    val versionString =
        if (includeVersion) {
            version
                .let { if (it.startsWith("v")) it else "v$it" }
                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
        } else {
            ""
        }
    return "${name.toPascalCase()}$versionString"
}
