package it.krzeminski.githubactions

import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.Action

infix fun Action.shouldHaveYamlArguments(yaml: LinkedHashMap<String, String>) {
    this.toYamlArguments().shouldBe(yaml)
}
