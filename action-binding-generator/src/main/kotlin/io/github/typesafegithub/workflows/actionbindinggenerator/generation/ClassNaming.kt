package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase

internal fun ActionCoords.buildActionClassName(): String = this.fullName.toPascalCase()
