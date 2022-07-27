package it.krzeminski.githubactions.dsl

import kotlinx.serialization.Contextual

interface HasCustomArguments {
    @Suppress("VariableNaming")
    val _customArguments: Map<String, @Contextual Any>
}
