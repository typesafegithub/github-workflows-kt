package io.github.typesafegithub.workflows.actionsmetadata.model

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionCoords

data class ActionBindingRequest(
    val actionCoords: ActionCoords,
)
