package it.krzeminski.githubactions

import io.kotest.matchers.maps.shouldContainExactly
import it.krzeminski.githubactions.actions.Action

infix fun Action.shouldHaveYamlArguments(yaml: Map<String, String>) {
    this.toYamlArguments().shouldContainExactly(yaml)
}
