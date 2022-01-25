package it.krzeminski.githubactions.actions.gradle

import it.krzeminski.githubactions.actions.Action

class WrapperValidationActionV1 : Action("gradle/wrapper-validation-action") {
    override fun toYamlArguments() = linkedMapOf<String, String>()
}
