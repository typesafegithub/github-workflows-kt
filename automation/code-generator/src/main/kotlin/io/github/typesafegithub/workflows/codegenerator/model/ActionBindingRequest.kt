package io.github.typesafegithub.workflows.codegenerator.model

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

data class ActionBindingRequest(
    val actionCoords: ActionCoords,
)
