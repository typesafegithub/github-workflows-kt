package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

data class BindingsServerRequest(
    val rawOwner: String,
    val rawName: String,
    val rawVersion: String?,
    val actionCoords: ActionCoords,
)
