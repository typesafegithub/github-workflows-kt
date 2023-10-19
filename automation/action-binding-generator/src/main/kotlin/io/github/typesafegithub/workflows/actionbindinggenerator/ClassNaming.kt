package io.github.typesafegithub.workflows.actionbindinggenerator

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
