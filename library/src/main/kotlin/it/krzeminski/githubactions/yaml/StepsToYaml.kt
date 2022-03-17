package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.fullName
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Step
import it.krzeminski.githubactions.dsl.ListFreeArg
import it.krzeminski.githubactions.dsl.StringFreeArg

fun List<Step>.stepsToYaml(): String =
    this.joinToString(separator = "\n") {
        it.toYaml()
    }

private fun Step.toYaml() =
    when (this) {
        is ExternalActionStep -> toYaml()
        is CommandStep -> toYaml()
    }

private fun ExternalActionStep.toYaml(): String = buildString {
    appendLine("- id: $id")
    appendLine("  name: $name")
    appendLine("  uses: ${action.fullName}")

    val allArguments = action.mergeArguments()
    if (allArguments.isNotEmpty()) {
        val arguments = allArguments.toYaml()
        appendLine("  with:")
        appendLine(arguments.prependIndent("    "))
    }
    if (this@toYaml.env.isNotEmpty()) {
        appendLine("  env:")
        appendLine(this@toYaml.env.toYaml().prependIndent("    "))
    }
    this@toYaml.condition?.let {
        appendLine(it.conditionToYaml())
    }
    action._customArguments.filterIsInstance<ListFreeArg>().forEach {
        printIfHasElements(it.value, it.key, "    ")
    }
}.removeSuffix("\n")

private fun CommandStep.toYaml() = buildString {
    appendLine("- id: $id")
    appendLine("  name: $name")

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

private fun Action.mergeArguments(): LinkedHashMap<String, String> {
    val freeArgsYaml = _customArguments.filterIsInstance<StringFreeArg>().map { it.key to it.value }.toMap()
    return LinkedHashMap(toYamlArguments() + freeArgsYaml)
}

private fun String.conditionToYaml() =
    "  if: $this"
