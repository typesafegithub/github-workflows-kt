package io.github.typesafegithub.workflows.actionbindinggenerator.domain

import io.github.typesafegithub.workflows.actionbindinggenerator.typing.Typing

public data class ActionTypings(
    val inputTypings: Map<String, Typing> = emptyMap(),
    val outputTypings: Map<String, Typing> = emptyMap(),
    val source: TypingActualSource? = null,
)
