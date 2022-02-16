package it.krzeminski.githubactions.wrappergenerator.domain

import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing

data class WrapperRequest(
    val coords: ActionCoords,
    val inputTypings: Map<String, Typing> = emptyMap(),
    val deprecated: Set<String> = emptySet(),
)
