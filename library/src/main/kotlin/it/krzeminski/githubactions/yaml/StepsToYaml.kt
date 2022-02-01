package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.fullName
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
    appendLine("  uses: ${action.fullName}")
    this@toYaml.action.argumentsToYaml().let { arguments ->
        if (arguments.isNotEmpty()) {
            appendLine("  with:")
            appendLine(arguments.prependIndent("    "))
        }
    }
    if (this@toYaml.env.isNotEmpty()) {
        appendLine("  env:")
        appendLine(this@toYaml.env.toYaml().prependIndent("    "))
    }
    this@toYaml.condition?.let {
        appendLine(it.conditionToYaml())
    }
}.removeSuffix("\n")

private fun CommandStep.toYaml() = buildString {
    appendLine("- name: $name")

    if (this@toYaml.env.isNotEmpty()) {
        appendLine("  env:")
        appendLine(this@toYaml.env.toYaml().prependIndent("    "))
    }

    if (command.lines().size == 1) {
        appendLine("  run: $command")
    } else {
        appendLine("  run: |")
        command.lines().forEach {
            appendLine(it.prependIndent("    "))
        }
    }

    this@toYaml.condition?.let {
        appendLine(it.conditionToYaml())
    }
}.removeSuffix("\n")

private fun Action.argumentsToYaml() =
    toYamlArguments().toYaml()

private fun String.conditionToYaml() =
    "  if: $this"
