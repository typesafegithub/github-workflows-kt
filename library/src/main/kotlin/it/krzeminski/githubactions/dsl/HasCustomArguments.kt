package it.krzeminski.githubactions.dsl

import kotlinx.serialization.Contextual

public interface HasCustomArguments {
    @Suppress("VariableNaming")
    public val _customArguments: Map<String, @Contextual Any?>
}
