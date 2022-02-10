package it.krzeminski.githubactions.wrappergenerator.generation

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import java.util.Locale

fun ActionCoords.buildActionClassName() =
    "${name.toPascalCase()}${version.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
