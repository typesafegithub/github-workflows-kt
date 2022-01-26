package it.krzeminski.githubactions.actions.gradleupdate

import it.krzeminski.githubactions.actions.Action

class UpdateGradleWrapperActionV1 : Action("gradle-update/update-gradle-wrapper-action@v1") {
    override fun toYamlArguments() = linkedMapOf<String, String>()
}
