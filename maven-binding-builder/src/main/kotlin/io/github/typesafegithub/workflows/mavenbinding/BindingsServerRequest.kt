package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1

data class BindingsServerRequest(
    val rawName: String,
    val rawVersion: String?,
    val bindingVersion: BindingVersion = V1,
    val actionCoords: ActionCoords,
)
