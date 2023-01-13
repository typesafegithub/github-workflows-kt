package it.krzeminski.githubactions.actionsmetadata.model

sealed interface TypingsSource {
    data class WrapperGenerator(val inputTypings: Map<String, Typing> = emptyMap()) : TypingsSource
    object ActionTypes : TypingsSource
}

data class WrapperRequest(
    val actionCoords: ActionCoords,
    val typingsSource: TypingsSource = TypingsSource.WrapperGenerator(),
)
