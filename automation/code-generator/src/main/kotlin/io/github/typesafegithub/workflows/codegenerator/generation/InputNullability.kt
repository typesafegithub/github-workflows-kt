package io.github.typesafegithub.workflows.codegenerator.generation

import io.github.typesafegithub.workflows.codegenerator.metadata.Input

/**
 * [Input.required] is in theory a required field in action's metadata, but in practice a lot of actions don't specify
 * it. It's thus a challenge to infer nullability of inputs in the Kotlin wrappers. This function tackles this task.
 */
fun Input.shouldBeNonNullInWrapper() =
    default == null && required == true
