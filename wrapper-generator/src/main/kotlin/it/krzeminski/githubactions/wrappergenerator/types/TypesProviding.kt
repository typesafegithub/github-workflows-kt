package it.krzeminski.githubactions.wrappergenerator.types

import it.krzeminski.githubactions.wrappergenerator.domain.TypingsSource
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing

fun WrapperRequest.provideTypes(): Map<String, Typing> =
    when (typingsSource) {
        is TypingsSource.WrapperGenerator -> typingsSource.inputTypings
        // TODO add reading from action-types.yml
    }
