package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Step

fun List<Step>.stepsToYaml(): String =
    this.joinToString(separator = "\n") {
        it.toYaml()
    }

private fun Step.toYaml() =
    when (this) {
        is ExternalActionStep -> toYaml()
        is CommandStep -> toYaml()
    }

private fun ExternalActionStep.toYaml() = buildString {
    appendLine("- name: $name")
    appendLine("  uses: ${action.name}")
    this@toYaml.condition?.let {
        appendLine(it.conditionToYaml())
    }
    this@toYaml.action.argumentsToYaml().let { arguments ->
        if (arguments.isNotEmpty()) {
            appendLine("  with:")
            appendLine(arguments.prependIndent("    "))
        }
    }
}.removeSuffix("\n")

private fun CommandStep.toYaml() = buildString {
    appendLine("- name: $name")
    this@toYaml.condition?.let {
        appendLine(it.conditionToYaml())
    }
    append("  run: $command")
}

private fun Action.argumentsToYaml() =
    toYamlArguments().map { (key, value) ->
        "$key: $value"
    }.joinToString(separator = "\n")

private fun String.conditionToYaml() =
    "  if: $this"
