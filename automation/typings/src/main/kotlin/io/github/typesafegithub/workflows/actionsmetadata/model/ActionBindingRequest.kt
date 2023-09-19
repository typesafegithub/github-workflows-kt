package io.github.typesafegithub.workflows.actionsmetadata.model

sealed interface TypingsSource {
    data class CodeGenerator(val inputTypings: Map<String, Typing> = emptyMap()) : TypingsSource
    object ActionTypes : TypingsSource
}

data class ActionBindingRequest(
    val actionCoords: ActionCoords,
    val typingsSource: TypingsSource = TypingsSource.CodeGenerator(),
)
