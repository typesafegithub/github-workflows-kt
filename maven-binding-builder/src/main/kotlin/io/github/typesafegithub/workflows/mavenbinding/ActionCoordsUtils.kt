package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName

internal val ActionCoords.mavenName: String get() = "$name${subName.replace("/", "__")}${
    significantVersion.takeUnless { it == FULL }?.let { "___${it.name.lowercase()}" } ?: ""
}"
