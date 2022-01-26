package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action

class WrapperValidationActionV1 : Action("gradle/wrapper-validation-action@v1") {
    override fun toYamlArguments() = linkedMapOf<String, String>()
}
